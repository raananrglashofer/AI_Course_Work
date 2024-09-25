import random
import NQueens
import time

def count_conflicts(columns):
    n = len(columns)
    conflicts = 0
    for i in range(n):
        for j in range(n):
            if i != j:
                if columns[i] == columns[j] or abs(columns[i] - columns[j]) == abs(i - j):
                    conflicts += 1
    return conflicts


def heuristic_repair_n_queens(n):
    number_of_iterations = 0
    number_of_moves = 0

    columns = NQueens.place_n_queens(n)
    number_of_moves += n
    prev_conflict = n
    stuckCount = 0
    while True:
        if stuckCount == 100:
            columns = NQueens.place_n_queens(n)
            number_of_moves += n
            stuckCount = 0
        number_of_iterations += 1
        conflicts = count_conflicts(columns)
        if conflicts == prev_conflict:
            stuckCount += 1
        else:
            stuckCount = 0
        prev_conflict = conflicts
        if conflicts == 0:
            return columns, number_of_iterations, number_of_moves

        row_to_move = random.randint(0, n - 1)
        min_conflicts = conflicts
        best_col = columns[row_to_move]

        for col in range(n):
            if col != columns[row_to_move]:
                new_columns = columns[:]
                new_columns[row_to_move] = col
                new_conflicts = count_conflicts(new_columns)

                if new_conflicts < min_conflicts:
                    min_conflicts = new_conflicts
                    best_col = col

        columns[row_to_move] = best_col
        number_of_moves += 1

# Run it
start = time.time()
columns, num_iterations, number_moves = heuristic_repair_n_queens(8)
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
for i in range(1, 100):
    print("This is run number: " + str(i))
    start = time.time()
    columns, num_iterations, number_moves = heuristic_repair_n_queens(8)
    end = time.time()
    time_taken = end - start
    print("Time elapsed: " + str(time_taken))
    sum_moves += number_moves
    sum_iteratiosn += num_iterations
    sum_time += time_taken

avg_moves = sum_moves / 100
avg_iteration = sum_iteratiosn / 100
avg_time = sum_time / 100

print(f"Average iterations: {avg_iteration}")
print(f"Average moves: {avg_moves}")
print(f"Average Time: {avg_time}")
'''