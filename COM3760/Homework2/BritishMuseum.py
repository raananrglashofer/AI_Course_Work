import NQueens

def british_museum_n_queens(n):
    number_of_iterations = 0
    number_of_moves = 0

    while True:
        # Randomly place queens
        columns = NQueens.place_n_queens(n)
        number_of_moves += n
        number_of_iterations += 1

        # Check if this random placement is valid
        if all(NQueens.is_place_in_next_row_valid(columns[i], columns[:i]) for i in range(n)):
            return columns, number_of_iterations, number_of_moves

# run it
columns, num_iterations, number_moves = british_museum_n_queens(8)
print(f"number of iterations: {num_iterations}")
print(f"number of moves: {number_moves}")
NQueens.displayBoard(columns)