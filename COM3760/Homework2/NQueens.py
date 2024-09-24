import random
def place_n_queens(n):
    assert n >= 0
    columns = [random.randrange(0,n) for x in range(n)]
    return columns


def displayBoard(columns):
    if not columns:
        return
    n = len(columns)
    print(columns)
    for rowVal in range(n):
        rowStr = ["." for x in range(n)]
        rowStr[columns[rowVal]] = '♛'
        print("  ".join(rowStr))
    print()

def place_in_next_row(col, columns):
    columns.append(col)

def remove_from_current_row(columns):
    if len(columns) > 0:
        return columns.pop()
    return -1

    # better version

def is_place_in_next_row_valid(col, columns):
    # new row
    row = len(columns)

    # for each row with existing queen check if the current (row, col) position is not valid

    for queen_row, queen_column in enumerate(columns):
        if col == queen_column:
            return False
        # check if col in new row is in the same diagonal as existing queen
        if queen_column - queen_row == col - row:
            return False
        # check if col in new row is in the same anti-diagonal as existing queen
        if queen_column + queen_row == col + row:
            return False

    return True

