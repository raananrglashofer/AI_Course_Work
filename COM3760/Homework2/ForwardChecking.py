import random
import time
import NQueens
import copy
import pandas as pd

def solve_queens_forward_checking(n):
    columns = []
    number_of_moves = 0
    number_of_iterations = 0
    row = 0
    stack = []
    open_spots = {row: list(range(n)) for row in range(n)}

    while True:
        if not open_spots[row]:
            open_spots = stack.pop()
            prev_col = NQueens.remove_from_current_row(columns)
            number_of_moves += 1
            row -= 1
            col = prev_col + 1
            continue

        col = random.choice(open_spots[row])

        number_of_iterations += 1
        if NQueens.is_place_in_next_row_valid(col, columns):
            NQueens.place_in_next_row(col, columns)
            number_of_moves += 1
            open_spots[row].remove(col)
            stack.append(copy.deepcopy(open_spots))


            for future_row in range(row + 1, n):
                if col in open_spots[future_row]:
                    open_spots[future_row].remove(col)
                diag_left = col - (future_row - row)
                diag_right = col + (future_row - row)
                if diag_left in open_spots[future_row]:
                    open_spots[future_row].remove(diag_left)
                if diag_right in open_spots[future_row]:
                    open_spots[future_row].remove(diag_right)


                if not open_spots[future_row]:
                    break

            row += 1
        else:
            open_spots[row].remove(col)

        if row == n:
            return columns, number_of_iterations, number_of_moves

        if not open_spots[row]:
            open_spots = stack.pop()
            prev_col = NQueens.remove_from_current_row(columns)
            number_of_moves += 1
            row -= 1
            continue

# Run it
start = time.time()
columns, num_iterations, number_moves = solve_queens_forward_checking(40)
end = time.time()
print(f"number of iterations: {num_iterations}")
print(f"number of moves: {number_moves}")
time_taken = end - start
print("Time is: " + str(time_taken))
NQueens.displayBoard(columns)

'''
sum_moves = 0
sum_iteratiosn = 0
sum_time = 0
for i in range(100):
    start = time.time()
    columns, num_iterations, number_moves = solve_queens_forward_checking(30)
    end = time.time()
    time_taken = end - start
    sum_moves += number_moves
    sum_iteratiosn += num_iterations
    sum_time += time_taken

avg_moves = sum_moves / 100
avg_iteration = sum_iteratiosn / 100
avg_time = sum_time / 100

print(f"Average iterations: {avg_iteration}")
print(f"Average moves: {avg_moves}")
print(f"Avergae Time: {avg_time}")
'''

