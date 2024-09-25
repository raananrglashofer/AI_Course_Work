import time
import NQueens
import copy
def solve_queens_forward_checking(n):
    columns = []
    number_of_moves = 0
    number_of_iterations = 0
    row = 0
    col = 0
    stack = []
    open_spots = {row: list(range(n)) for row in range(n)}
    while True:
        while col < n:
            #print("start loop")
            #print(open_spots)
           # time.sleep(1.5)
            number_of_iterations += 1
            if NQueens.is_place_in_next_row_valid(col, columns):
                #print("Placed Queen in " + str(row) + "," + str(col))
                NQueens.place_in_next_row(col, columns)
                number_of_moves += 1
                open_spots[row].remove(col)
                # append previous state with current row removed
                stack.append(copy.deepcopy(open_spots))
                # update map
                for future_row in range(row + 1, n):
                    if col in open_spots[future_row]:
                        open_spots[future_row].remove(col)
                    # Remove diagonals
                    diag_left = col - (future_row - row)
                    diag_right = col + (future_row - row)
                    if diag_left in open_spots[future_row]:
                        open_spots[future_row].remove(diag_left)
                    if diag_right in open_spots[future_row]:
                        open_spots[future_row].remove(diag_right)
                row += 1
                col = 0
                break
            else:
                col += 1
        if (col == n or row == n or not open_spots[col]):
            number_of_iterations += 1
            # if board is full, we have a solution
            if row == n:
                # print("I did it! Here is my solution")
                # display(columns)
                return columns, number_of_iterations, number_of_moves
            # else couldn't find a solution so need to backtrack
            #print("start to backtrack ... ")
            # pop stack here - previous state
            open_spots = stack.pop()
            prev_col = NQueens.remove_from_current_row(columns)
            number_of_moves += 1
            if (prev_col == -1):  # backtracked past column 1
                print("There are no solutions")
                # print(number_of_moves)
                return columns, number_of_iterations, number_of_moves
            # retry previous row again
            row -= 1
            # start to now check at col = (1 + value of prev_column in the row)
            col = 1 + prev_col
# run it
start = time.time()
columns, num_iterations, number_moves = solve_queens_forward_checking(30)
end = time.time()
print(f"number of iterations: {num_iterations}")
print(f"number of moves: {number_moves}")
time = end - start
print("Time is: " + str(time))
NQueens.displayBoard(columns)
