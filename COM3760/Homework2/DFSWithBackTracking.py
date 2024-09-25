import NQueens
def solve_queens_dfs(size):
    columns = []
    number_of_moves = 0
    number_of_iterations = 0
    row = 0  # current row
    col = 0  # always start each row at leftmost col
    # iterate over rows of board
    while True:
        if number_of_iterations > 10000000:
            return number_of_moves, number_of_iterations
        # place queen in next row
        # print("I am trying to put the queen in row ", row)
        # print(columns)
        # print(f"number_of_moves: {number_of_moves}")
        while col < size:
            number_of_iterations += 1
            if NQueens.is_place_in_next_row_valid(col, columns):
                NQueens.place_in_next_row(col, columns)
                number_of_moves += 1
                # print(f"placed: row: {row}, col: {col}")
                row += 1
                col = 0
                break
            else:
                # print(f"not placed: row: {row}, col: {col}")
                col += 1

        # could not find an open col in this row,  or board is full
        if (col == size or row == size):
            number_of_iterations += 1
            # if board is full, we have a solution
            if row == size:
                # print("I did it! Here is my solution")
                # display(columns)
                converged = True
                return columns, number_of_iterations, number_of_moves, converged
            # else couldn't find a solution so need to backtrack
            # print("start to backtrack ... ")
            prev_col = NQueens.remove_from_current_row(columns)
            number_of_moves += 1
            if (prev_col == -1):  # backtracked past column 1
                print("There are no solutions")
                # print(number_of_moves)
                converged = False
                return columns, number_of_iterations, number_of_moves, converged
            # retry previous row again
            row -= 1
            # start to now check at col = (1 + value of prev_column in the row)
            col = 1 + prev_col
# run it
columns, num_iterations, number_moves, converged = solve_queens_dfs(20)
print(f"number of iterations: {num_iterations}")
print(f"number of moves: {number_moves}")
NQueens.displayBoard(columns)