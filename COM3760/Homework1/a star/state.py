'''
The state is a list of 2 items: the board, the path
The target for 8-puzzle is: (zero is the hole)
012
345
678
'''
import random
import math

#returns a random board nXn
def create(n):
    s=list(range(n*n))      # s is the board itself. a vector that represent a matrix. s=[0,1,2....n^2-1]
    m="<>v^"                # m is "<>v^" - for every possible move (left, right, down, up)
    for i in range(n**3):  # makes n^3 random moves to mix the tiles
        if_legal(s,m[random.randrange(4)])
    return [s,""]           # at the beginning "" is an empty path, later on path
                            # contains the path that leads from the initial state to the state

def get_next(x):            # returns a list of the children states of x
    ns=[]                   # the next state list
    for i in "<>v^":
        s=x[0][:]           # [:] - copies the board in x[0]
        if_legal(s,i)       # try to move in direction i
        # checks if the move was legal and...
        if s.index(0)!=x[0].index(0) and \
           (x[1]=="" or x[1][-1]!="><^v"["<>v^".index(i)]): # check if it's the first move or it's a reverse move
            ns.append([s,x[1]+i])   # appends the new state to ns
    return ns


def path_len(x):
    return len(x[1])

def is_target(x):
    n=len(x[0])                     # the size of the board
    return x[0]==list(range(n))     # list(range(n)) is the target state

#############################
def if_legal(x,m):                  # gets a board and a move and makes the move if it's legal
    n=int(math.sqrt(len(x)))        # the size of the board is nXn
    z=x.index(0)                    # z is the place of the empty tile (0)
    if z%n>0 and m=="<":            # checks if the empty tile is not in the first col and the move is to the left
        x[z]=x[z-1]                 # swap x[z] and x[z-1]...
        x[z-1]=0                    # ...and move the empty tile to the left
    elif z%n<n-1 and m==">":        # check if the empty tile is not in the n's col and the move is to the right
        x[z]=x[z+1]
        x[z+1]=0
    elif z>=n and m=="^":           # check if the empty tile is not in the first row and the move is up
        x[z]=x[z-n]
        x[z-n]=0
    elif z<n*n-n and m=="v":        # check if the empty tile is not in the n's row and the move is down
        x[z]=x[z+n]
        x[z+n]=0

# This is your HW
def hdistance0(s):                   # the heuristic value of s -- uniform cost
    return 0

def hdistance1(s):
    misplaced_tiles = 0
    n = len(s[0])
    for i in range(n):
        if s[0][i] != i and s[0][i] != 0:
            misplaced_tiles += 1
    return misplaced_tiles

def hdistance2(s):
    coordinate_map = {
        0: [0, 0],
        1: [0, 1],
        2: [0, 2],
        3: [0, 3],
        4: [1, 0],
        5: [1, 1],
        6: [1, 2],
        7: [1, 3],
        8: [2, 0],
        9: [2, 1],
        10: [2, 2],
        11: [2, 3],
        12: [3, 0],
        13: [3, 1],
        14: [3, 2],
        15: [3, 3]
    }
    manhattan_dist = 0
    n = len(s[0])
    for i in range(n):
        if s[0][i] != 0:
            current = coordinate_map.get(s[0][i])
            shouldBe = coordinate_map.get(i)
            difference = abs(current[0] - shouldBe[0]) + abs(current[1] - shouldBe[1])
            manhattan_dist += difference
    return manhattan_dist

def hdistance3(s):
    size = int(len(s[0]) ** 0.5)
    manhattan_dist = 0
    linear_conflict = 0

    coordinate_map = {
        0: [0, 0],
        1: [0, 1],
        2: [0, 2],
        3: [0, 3],
        4: [1, 0],
        5: [1, 1],
        6: [1, 2],
        7: [1, 3],
        8: [2, 0],
        9: [2, 1],
        10: [2, 2],
        11: [2, 3],
        12: [3, 0],
        13: [3, 1],
        14: [3, 2],
        15: [3, 3]
    }

    for i in range(len(s[0])):
        if s[0][i] != 0:
            current = coordinate_map[s[0][i]]
            shouldBe = coordinate_map[i]
            manhattan_dist += abs(current[0] - shouldBe[0]) + abs(current[1] - shouldBe[1])

    for row in range(size):
        row_tiles = [s[0][i] for i in range(row * size, (row + 1) * size)]
        linear_conflict += calculate_linear_conflict(row_tiles, row, coordinate_map, 'row')

    for col in range(size):
        col_tiles = [s[0][i * size + col] for i in range(size)]
        linear_conflict += calculate_linear_conflict(col_tiles, col, coordinate_map, 'col')

    return manhattan_dist + 2 * linear_conflict


def calculate_linear_conflict(tiles, index, coordinate_map, axis):
    conflict_count = 0

    for i in range(len(tiles)):
        for j in range(i + 1, len(tiles)):
            if tiles[i] != 0 and tiles[j] != 0:
                goal_pos_i = coordinate_map[tiles[i]]
                goal_pos_j = coordinate_map[tiles[j]]

                if axis == 'row' and goal_pos_i[0] == goal_pos_j[0] == index:
                    if goal_pos_i[1] > goal_pos_j[1] and i < j:
                        conflict_count += 1

                if axis == 'col' and goal_pos_i[1] == goal_pos_j[1] == index:
                    if goal_pos_i[0] > goal_pos_j[0] and i < j:
                        conflict_count += 1

    return conflict_count

