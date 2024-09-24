import random
import NQueens




def count_conflicts(columns):
    """Helper function to count the number of queens under attack."""
    n = len(columns)
    conflicts = 0
    for i in range(n):
        for j in range(i + 1, n):
            if columns[i] == columns[j] or abs(columns[i] - columns[j]) == abs(i - j):
                conflicts += 1
    return conflicts


def heuristic_repair_n_queens(n):
    number_of_iterations = 0
    number_of_moves = 0

    # Initial random placement
    columns = NQueens.place_n_queens(n)
    number_of_moves += n
    prev_conflict = n
    stuckCount = 0
    while True:
        if stuckCount == 10:
            columns = NQueens.place_n_queens(n)
            number_of_moves += n
            stuckCount = 0
        number_of_iterations += 1
        # Count conflicts
        conflicts = count_conflicts(columns)
        if conflicts == prev_conflict:
            stuckCount += 1
        else:
            stuckCount = 0
        prev_conflict = conflicts
        if conflicts == 0:
            # No conflicts, solution found
            return columns, number_of_iterations, number_of_moves

        # Repair step: pick a queen at random and move it to minimize conflicts
        row_to_move = random.randint(0, n - 1)
        min_conflicts = conflicts
        best_col = columns[row_to_move]

        # Try moving the queen in the selected row to each column and pick the best move
        for col in range(n):
            if col != columns[row_to_move]:
                new_columns = columns[:]
                new_columns[row_to_move] = col
                new_conflicts = count_conflicts(new_columns)

                if new_conflicts < min_conflicts:
                    min_conflicts = new_conflicts
                    best_col = col

        # Move the queen to the best column
        columns[row_to_move] = best_col
        number_of_moves += 1

columns, num_iterations, number_moves = heuristic_repair_n_queens(8)
print(f"number of iterations: {num_iterations}")
print(f"number of moves: {number_moves}")
NQueens.displayBoard(columns)
