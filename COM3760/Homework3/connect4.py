#connect4.py
import copy
import numpy as np
import random
from termcolor import colored  # can be taken out if you don't like it...

# # # # # # # # # # # # # # global values  # # # # # # # # # # # # # #
ROW_COUNT = 6
COLUMN_COUNT = 7

RED_CHAR = colored('X', 'red')  # RED_CHAR = 'X'
BLUE_CHAR = colored('O', 'blue')  # BLUE_CHAR = 'O'

EMPTY = 0
RED_INT = 1
BLUE_INT = 2


# # # # # # # # # # # # # # functions definitions # # # # # # # # # # # # # #

def create_board():
    """creat empty board for new game"""
    board = np.zeros((ROW_COUNT, COLUMN_COUNT), dtype=int)
    return board


def drop_chip(board, row, col, chip):
    """place a chip (red or BLUE) in a certain position in board"""
    board[row][col] = chip


def is_valid_location(board, col):
    """check if a given column in the board has a room for extra dropped chip"""
    return board[ROW_COUNT - 1][col] == 0


def get_next_open_row(board, col):
    """assuming column is available to drop the chip,
    the function returns the lowest empty row  """
    for r in range(ROW_COUNT):
        if board[r][col] == 0:
            return r


def print_board(board):
    """print current board with all chips put in so far"""
    # print(np.flip(board, 0))
    print(" 1 2 3 4 5 6 7 \n" "|" + np.array2string(np.flip(np.flip(board, 1)))
          .replace("[", "").replace("]", "").replace(" ", "|").replace("0", "_")
          .replace("1", RED_CHAR).replace("2", BLUE_CHAR).replace("\n", "|\n") + "|")


def game_is_won(board, chip):
    """check if current board contain a sequence of 4-in-a-row of in the board
     for the player that play with "chip"  """

    winning_Sequence = np.array([chip, chip, chip, chip])
    # Check horizontal sequences
    for r in range(ROW_COUNT):
        if "".join(list(map(str, winning_Sequence))) in "".join(list(map(str, board[r, :]))):
            return True
    # Check vertical sequences
    for c in range(COLUMN_COUNT):
        if "".join(list(map(str, winning_Sequence))) in "".join(list(map(str, board[:, c]))):
            return True
    # Check positively sloped diagonals
    for offset in range(-2, 4):
        if "".join(list(map(str, winning_Sequence))) in "".join(list(map(str, board.diagonal(offset)))):
            return True
    # Check negatively sloped diagonals
    for offset in range(-2, 4):
        if "".join(list(map(str, winning_Sequence))) in "".join(list(map(str, np.flip(board, 1).diagonal(offset)))):
            return True


def get_valid_locations(board):
    valid_locations = []
    for col in range(COLUMN_COUNT):
        if is_valid_location(board, col):
            valid_locations.append(col)
    return valid_locations


def MoveRandom(board, color):
    valid_locations = get_valid_locations(board)
    column = random.choice(valid_locations)  # you can replace with input if you like...
    row = get_next_open_row(board, column)
    drop_chip(board, row, column, color)

def refined_heuristic(board, color):
    opponent_color = RED_INT if color == BLUE_INT else BLUE_INT

    #Score for central column control
    center_column = [board[r][COLUMN_COUNT // 2] for r in range(ROW_COUNT)]
    center_count = center_column.count(color)
    score = center_count / 2

    # Add score for current player's contiguous pieces
    score += evaluate_contiguous_pieces(board, color, weights={1: 0.1, 2: 0.3, 3: 1.0, 4: 1000})

    # Subtract score for opponent's contiguous pieces
    score -= evaluate_contiguous_pieces(board, opponent_color, weights={1: 0.1, 2: 0.3, 3: 1.2, 4: 1000})

    score -= prioritize_blocking(board, opponent_color)

    score -= check_double_trap(board, opponent_color)

    return score

def prioritize_blocking(board, opponent_color):
    blocking_score = 0

    # Check all possible columns to see if the opponent can win in the next move
    for col in range(COLUMN_COUNT):
        if board[ROW_COUNT - 1, col] == 0:  # Ensure column is not full
            temp_board = board.copy()
            row = get_next_open_row(temp_board, col)
            drop_chip(temp_board, row, col, opponent_color)
            if game_is_won(temp_board, opponent_color):  # If opponent can win
                blocking_score += 10  # High penalty to prioritize blocking

    return blocking_score

def check_double_trap(board, opponent_color):
    penalty = 0

    # Check for horizontal double traps
    for r in range(ROW_COUNT):
        row = board[r, :]
        for c in range(COLUMN_COUNT - 3):
            if row[c] == EMPTY and row[c+1] == opponent_color and row[c+2] == opponent_color and row[c+3] == EMPTY:
                penalty += 1  # Penalty for potential double trap

    return penalty

def evaluate_contiguous_pieces(board, color, weights):
    total_value = 0

    def count_contiguous_pieces(line):
        count = 0
        max_value = 0
        for cell in line:
            if cell == color:
                count += 1
            else:
                if cell == EMPTY and count > 0:  # Open space to extend
                    max_value = max(max_value, weights.get(count, 0))
                count = 0
        return max_value

    # Check horizontal lines
    for r in range(ROW_COUNT):
        total_value += count_contiguous_pieces(board[r, :])

    # Check vertical lines
    for c in range(COLUMN_COUNT):
        total_value += count_contiguous_pieces(board[:, c])

    # Check diagonals
    for offset in range(-2, 4):
        total_value += count_contiguous_pieces(board.diagonal(offset))
        total_value += count_contiguous_pieces(np.flip(board, 1).diagonal(offset))

    return total_value

def heuristic(board, color):
    my_material = evaluate_contiguous_pieces(board, color)
    opponent_material = evaluate_contiguous_pieces(board, RED_INT if color == BLUE_INT else BLUE_INT)
    return my_material - opponent_material
def minimax_alpha_beta(board, depth, alpha, beta, maximizingPlayer, color):
    if game_is_won(board, RED_INT) or game_is_won(board, BLUE_INT) or depth == 0:
        return refined_heuristic(board, color), None

    valid_locations = get_valid_locations(board)
    best_move = None

    if maximizingPlayer:
        max_eval = float('-inf')
        for col in valid_locations:
            temp_board = board.copy()
            row = get_next_open_row(temp_board, col)
            drop_chip(temp_board, row, col, color)
            eval, _ = minimax_alpha_beta(temp_board, depth - 1, alpha, beta, False, color)
            if eval > max_eval:
                max_eval = eval
                best_move = col
            alpha = max(alpha, eval)
            if beta <= alpha:
                break  # Prune the branch
        return max_eval, best_move

    else:
        min_eval = float('inf')
        opponent_color = RED_INT if color == BLUE_INT else BLUE_INT
        for col in valid_locations:
            temp_board = board.copy()
            row = get_next_open_row(temp_board, col)
            drop_chip(temp_board, row, col, opponent_color)
            eval, _ = minimax_alpha_beta(temp_board, depth - 1, alpha, beta, True, color)
            if eval < min_eval:
                min_eval = eval
                best_move = col
            beta = min(beta, eval)
            if beta <= alpha:
                break  # Prune the branch
        return min_eval, best_move

def get_best_move_alpha_beta(board, color, depth=4):
    _, best_move = minimax_alpha_beta(board, depth, float('-inf'), float('inf'), True, color)
    return best_move


def minimax(board, depth, maximizingPlayer, color):
    if game_is_won(board, RED_INT) or game_is_won(board, BLUE_INT) or depth == 0:
        return refined_heuristic(board, color), None

    valid_locations = get_valid_locations(board)
    best_move = None

    if maximizingPlayer:
        max_eval = float('-inf')
        for col in valid_locations:
            temp_board = board.copy()
            row = get_next_open_row(temp_board, col)
            drop_chip(temp_board, row, col, color)
            eval, _ = minimax(temp_board, depth - 1, False, color)
            if eval > max_eval:
                max_eval = eval
                best_move = col
        return max_eval, best_move

    else:
        min_eval = float('inf')
        opponent_color = RED_INT if color == BLUE_INT else BLUE_INT
        for col in valid_locations:
            temp_board = board.copy()
            row = get_next_open_row(temp_board, col)
            drop_chip(temp_board, row, col, opponent_color)
            eval, _ = minimax(temp_board, depth - 1, True, color)
            if eval < min_eval:
                min_eval = eval
                best_move = col
        return min_eval, best_move


def get_best_move(board, color, depth=7):
    _, best_move = minimax(board, depth, True, color)
    return best_move

def MoveStrategic(board, color):
    col = get_best_move_alpha_beta(board, color)
    if col is not None:
        row = get_next_open_row(board, col)
        drop_chip(board, row, col, color)



# # # # # # # # # # # # # # main execution of the game # # # # # # # # # # # # # #
turn = 0

board = create_board()
print_board(board)
game_over = False

while not game_over:
    if turn % 2 == 0:
        col = int(input("RED please choose a column(1-7): "))
        while col > 7 or col < 1:
            col = int(input("Invalid column, pick a valid one: "))
        while not is_valid_location(board, col - 1):
            col = int(input("Column is full. pick another one..."))
        col -= 1

        row = get_next_open_row(board, col)
        drop_chip(board, row, col, RED_INT)

    if turn % 2 == 1 and not game_over:
        #MoveRandom(board, BLUE_INT)
        MoveStrategic(board, BLUE_INT)

    print_board(board)

    if game_is_won(board, RED_INT):
        game_over = True
        print(colored("Red wins!", 'red'))
    if game_is_won(board, BLUE_INT):
        game_over = True
        print(colored("Blue wins!", 'blue'))
    if len(get_valid_locations(board)) == 0:
        game_over = True
        print(colored("Draw!", 'blue'))
    turn += 1

# tmp = copy.deepcopy(board)