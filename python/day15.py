from __future__ import annotations

import sys
from enum import Enum
from typing import List, Set, Dict, Optional, Callable


class Entity(Enum):
    UNKNOWN = " "
    EMPTY = "."
    WALL = "#"
    OXYGEN = "@"


class Tile:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __add__(self, direction: Direction) -> Tile:
        return Tile(self.x + direction.x, self.y + direction.y)

    def __repr__(self):
        return f"Tile{str(self)}"

    def __str__(self):
        return f"({self.x},{self.y})"

    def __hash__(self):
        return hash((self.x, self.y))

    def __eq__(self, other) -> bool:
        return self.x == other.x and self.y == other.y


class Droid:

    def __init__(self):
        self.position = Tile(0, 0)
        self.world = World()
        self.last_move: Optional[Direction] = None

    def move(self) -> Optional[int]:
        path_to_destination: List[Tile] = self.world.nearest_unexplored_tile(self.position)
        if not path_to_destination:
            return None
        direction: Direction = {self.position + d: d for d in Direction}[path_to_destination[1]]
        self.last_move = direction
        return direction.code

    def update(self, movement: int):
        movement_result = MovementResult(movement)
        if movement_result == MovementResult.HIT_WALL:
            wall = self.position + self.last_move
            self.world.add(Entity.WALL, wall)
        elif movement_result == MovementResult.MOVED_STEP:
            self.position = self.position + self.last_move
            self.world.add(Entity.EMPTY, self.position)
        elif movement_result == MovementResult.MOVED_STEP_FOUND_OXYGEN:
            self.position = self.position + self.last_move
            self.world.add(Entity.OXYGEN, self.position)


class World:

    def __init__(self):
        self.known_map: Dict[Tile, Entity] = {}

    def nearest_unexplored_tile(self, position: Tile) -> List[Tile]:
        return self.find_tile(position, position, lambda tile, entity: entity is Entity.UNKNOWN)

    def find_tile(self, start: Tile, destination: Tile, matches: Callable[[Tile, Entity], bool]) -> List[Tile]:
        open_tiles: Dict[Tile, Node] = {start: Node(start)}
        explored_tiles: Set[Tile] = set()

        def find_matching_tile(direction: Direction) -> Optional[List[Tile]]:
            tile: Tile = current.tile + direction
            entity: Entity = self.known_map.get(tile, Entity.UNKNOWN)
            cost: int = current.cost + 1
            if matches(tile, entity):
                return current.get_path() + [tile]
            elif entity is Entity.WALL or tile in explored_tiles:
                pass
            elif tile not in open_tiles:
                open_tiles[tile] = Node(tile, cost, current)
            else:
                existing_cost = open_tiles[tile].cost
                found_shorter_route = existing_cost > cost
                if found_shorter_route:
                    open_tiles[tile] = Node(tile, cost, current)
            return None

        while len(open_tiles) > 0:
            current = min(open_tiles.values(), key=lambda node: node.cost_to(destination))
            del open_tiles[current.tile]
            explored_tiles.add(current.tile)
            result: Optional[List[Tile], Direction] = find_matching_tile(Direction.NORTH) or \
                find_matching_tile(Direction.EAST) or \
                find_matching_tile(Direction.SOUTH) or \
                find_matching_tile(Direction.WEST)
            if result is not None:
                return result
        return []  # no tile found

    def add(self, entity: Entity, position: Tile):
        self.known_map[position] = entity


class Direction(Enum):
    NORTH = (1, 0, 1)
    EAST = (2, 1, 0)
    SOUTH = (3, 0, -1)
    WEST = (4, -1, 0)

    def __init__(self, code: int, x: int, y: int):
        self.code = code
        self.x = x
        self.y = y

    @staticmethod
    def of(code: int):
        return {direction.code: direction for direction in Direction}[code]


class MovementResult(Enum):
    HIT_WALL = 0
    MOVED_STEP = 1
    MOVED_STEP_FOUND_OXYGEN = 2


class Node:
    def __init__(self, tile: Tile, cost: int = 0, parent: Optional[Node] = None):
        self.tile = tile
        self.parent = parent
        self.cost = cost

    def get_path(self):
        parents: List[Tile] = [self.tile]
        parent = self.parent
        while parent is not None:
            parents.append(parent.tile)
            parent = parent.parent
        parents.reverse()
        return parents

    def cost_to(self, target: Tile) -> int:
        return self.cost + abs(self.tile.x - target.x) + abs(self.tile.y - target.y)


def draw(world: Dict[Tile, Entity]):
    min_x, min_y = sys.maxsize, sys.maxsize
    max_x, max_y = -sys.maxsize, -sys.maxsize
    for tile in world.keys():
        if tile.x < min_x:
            min_x = tile.x
        if tile.y < min_y:
            min_y = tile.y
        if tile.x > max_x:
            max_x = tile.x
        if tile.y > max_y:
            max_y = tile.y
    for y in range(min_y, max_y + 1):
        for x in range(min_x, max_x + 1):
            entity = world.get(Tile(x, y), " ")
            print(Entity(entity).value, end="")
        print()


file = open("map.txt", "r")
if file.mode == "r":
    contents = file.readlines()
    droid = Droid()
    droid.position = Tile(2, 2)
    while True:
        command: Optional[int] = droid.move()
        if command is None:
            draw(droid.world.known_map)
            exit(0)
        destination: Tile = droid.position + Direction.of(command)
        result: str = contents[destination.y][destination.x]
        if result == Entity.EMPTY.value:
            droid.update(MovementResult.MOVED_STEP.value)
        elif result == Entity.WALL.value:
            droid.update(MovementResult.HIT_WALL.value)
        elif result == Entity.OXYGEN.value:
            droid.update(MovementResult.MOVED_STEP_FOUND_OXYGEN.value)
