import state
import frontier
import search

TOTAL_RUNS = 100
MAX_PUSH_LIMIT = 2000  # Limit where the algorithm stops if it exceeds


def run_search(description):
    total_pushes = []
    total_pops = []
    path_costs = []

    for i in range(TOTAL_RUNS):
        # Generate a random 4x4 state
        start_state = state.create(4)

        # Initialize the frontier and start the search
        search_result = search_with_limit(start_state)

        if search_result:
            solution, pushed, max_states = search_result
            total_pushes.append(pushed)
            total_pops.append(max_states)
            path_costs.append(state.path_len(solution))
        else:
            # If no result was found (e.g., hit the limit)
            total_pushes.append(MAX_PUSH_LIMIT)
            total_pops.append(MAX_PUSH_LIMIT)
            path_costs.append(MAX_PUSH_LIMIT)

    # Save the results to a file
    save_results(total_pushes, total_pops, path_costs, description)


def search_with_limit(start_state):
    f = frontier.create(start_state)

    while not frontier.is_empty(f):
        s = frontier.remove(f)

        # Check if the state is the target
        if state.is_target(s):
            return [s, f[1], f[3]]  # [solution, total pushed, max states]

        # Get the next possible states
        ns = state.get_next(s)

        for i in ns:
            frontier.insert(f, i)

        # Check if we've exceeded the push limit
        if f[1] > MAX_PUSH_LIMIT:
            return None  # Return None if we hit the limit

    return None  # Return None if no solution was found


# Save the results into a .txt file
def save_results(pushed, popped, path_costs, description):
    with open('results.txt', 'a') as f:
        f.write(f"\n{description}:\n")
        f.write(f"Total number of independent runs: {len(pushed)}\n")
        f.write(f"Average total items pushed: {sum(pushed) / len(pushed):.2f}\n")
        f.write(f"Average total items popped: {sum(popped) / len(popped):.2f}\n")
        f.write(f"Average path cost of solution: {sum(path_costs) / len(path_costs):.2f}\n")
        f.write(f"Max total items pushed: {max(pushed)}\n")
        f.write(f"Max total items popped: {max(popped)}\n")
        f.write(f"Max path cost of solution: {max(path_costs)}\n")


# Main function to run the searches
if __name__ == "__main__":
        run_search('Linear Conflict')  # First run with hdistance0
    # Manually switch heuristic in frontier.py and re-run this script for the other heuristics.
