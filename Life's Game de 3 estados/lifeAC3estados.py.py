# life.py simulates John Conway's Game of Life with random initial states
# -----------------------------------------------------------------------------
import sys, pygame, math
from pygame.locals import *
from threading import Thread

# -----------------------------------------------------------------------------
# GLOBALS
# The title and version of this program
title, version = "The Game of Life", "1.0"

# The dimensions of each cell (in pixels)
cell_dimensions = (10, 10)

# The framerate of the game (in milliseconds)
framerate = 30

# The fraction of the board occupied by cells when randomly generated
occupancy = 0.20

# Colors used to represent the cells
colors = {0: (130, 122, 120), 1: (68,121,70), 2: (68,97,121)}


# -----------------------------------------------------------------------------
# CLASSES
#
class testit(Thread):
    def __init__(self):
        Thread.__init__(self)

    def __init__(self, args):
        Thread.__init__(self)
        self.args = args

    def run(self):
        main(self.args)

# -----------------------------------------------------------------------------
# FUNCTIONS

# Main function
def main(args):
    # Get the board dimensions (in cells, not pixels) from command-line input
    if len(args) != 3:
        board_dimensions = (101, 101)
    else:
        board_dimensions = (int(args[1]), int(args[2]))

    # Initialize pygame elements
    screen, bg, clock = init(board_dimensions)

    # Initialize random board
    board = central_cruice_board(board_dimensions)
    
    # Enter the game loop
    quit_game = False
    # Draw the board on the background
    while not quit_game:

        # Slow things down to match the framerate
        clock.tick(framerate)

        # Update the board
        update_board(board, board_dimensions)
        # print

        # Draw the board on the background
        draw_board(board, bg)
        # Blit bg to the screen, flip display buffers
        screen.blit(bg, (0, 0))
        pygame.display.flip()

        # Queue user input to catch QUIT signals
        for e in pygame.event.get():
            if e.type == QUIT:
                quit_game = True

    # Print farewell message
    print("Thanks for watching!")

def central_cruice_board(board_dimensions):
    board = dict()
    for x in range(board_dimensions[0]):
        for y in range(board_dimensions[1]):
            board[(x, y)] = 0
    
    middle = math.floor(board_dimensions[0] / 2), math.floor(board_dimensions[1] / 2)
    board[(middle[0], middle[1] - 1)] = 1 # up
    board[(middle[0], middle[1] + 1)] = 1 # down
    board[(middle[0] - 1, middle[1])] = 1 # left
    board[(middle[0] + 1, middle[1])] = 1 # right
    return board 

# Initialize pygame elements
def init(board_dimensions):
    # Grab hard-coded global values
    global title, version, cell_dimensions

    # Initialize the pygame modules
    pygame.init()

    # Determine and set the screen dimensions
    dimensions = (board_dimensions[0] * cell_dimensions[0],
                  board_dimensions[1] * cell_dimensions[1])
    screen = pygame.display.set_mode(dimensions)

    # Set the title string of the root window
    pygame.display.set_caption(title + " " + version)

    # Grab the background surface of the screen
    bg = screen.convert()

    # Grab the game clock
    clock = pygame.time.Clock()

    # Return the screen, the background surface, and the game clock
    return screen, bg, clock

# Update the board according to the rules of the game
def update_board(board, board_dimensions):

    # For every cell in the board...
    for cell in board:
        # How many occupied neighbors does this cell have?
        death, alive1, alive2 = count_neighbors(cell, board, board_dimensions)
        estadoCelula = board[cell]
        '''
            AB, A: actual state, B: new state
        '''
        if alive1 < (3 - estadoCelula):
            if estadoCelula == 1:
                board[cell] = 10    # from state 1 to 0
            elif estadoCelula == 2:
                board[cell] = 20    # from state 2 to 0
        
        elif alive1 == (3 - estadoCelula) or alive1 == (4 - estadoCelula):
            if estadoCelula == 0:
                board[cell] = -1    # from state 0 to 1
            elif estadoCelula == 2:
                board[cell] = 21    # from state 2 to 1

        elif death == 5:
            if estadoCelula == 1:
                board[cell] = 10    # from state 1 to 0
            elif estadoCelula == 2:
                board[cell] = 20    # from state 2 to 0

        elif alive1 >= 4 or death >= 4: 
            if estadoCelula == 0:
                board[cell] = -2    # from state 0 to 2
            elif estadoCelula == 1:
                board[cell] = 12    # from state 1 to 2

        elif alive2 >= 3:
            if estadoCelula == 1:
                board[cell] = 10    # from state 1 to 0
            elif estadoCelula == 2:
                board[cell] = 20    # from state 2 to 0
        
        elif alive2 == 2:
            if estadoCelula == 0:
                board[cell] = -1    # from state 0 to 1
            elif estadoCelula == 2:
                board[cell] = 21    # from state 2 to 1

    # Now, go through it again, making all the approved changes
    for cell in board:
        if board[cell] in [10, 20]:
            board[cell] = 0
        if board[cell] in [-1, 21]:
            board[cell] = 1
        if board[cell] in [-2, 12]: 
            board[cell] = 2

# Return the number of occupied neighbors this cell has
def count_neighbors(cell, board, board_dimensions):
    # Figure out the potential neighboring cells (need to watch the edges)
    i, j = cell[0], cell[1]
    
    left = i-1
    if left < 0:
        left = board_dimensions[0] - 1
    right = i+1
    if right >= board_dimensions[0]:
        right = 0
    top = j-1
    if top < 0:
        top = board_dimensions[1] - 1
    bot = j+1
    if bot >= board_dimensions[1]:
        bot = 0
    
    neighbors = [(left, top),(i, top),(right, top),
                 (left, j),             (right, j),
                 (left, bot),(i, bot),(right, bot)]

    # For each potential neighbor, if the cell is occupied add one to the score
    death, alive1, alive2 = 0, 0, 0
    for neighbor in neighbors:
        # Is this a real neighbor, or is it out-of-bounds?
        if neighbor in board.keys():
            # Remember that neighbors which are marked for death count, too!
            if board[neighbor] in [0,-1,-2]: death += 1
            if board[neighbor] in [1,10,12]: alive1 += 1
            if board[neighbor] in [2,20,21]: alive2 += 1

    # Return the score
    return (death, alive1, alive2)

# Draw the board on the background
def draw_board(board, bg):
    # Grab hard-coded global values
    global cell_dimensions

    # Draw every cell in the board as a rectangle on the screen
    for cell in board:
        rectangle = (cell[0] * cell_dimensions[0], cell[1] * cell_dimensions[1],
                     cell_dimensions[0], cell_dimensions[1])
        pygame.draw.rect(bg, colors[board[cell]], rectangle)

# -----------------------------------------------------------------------------
# The following code is executed upon command-line invocation
if __name__ == "__main__":
    program = testit(sys.argv)
    program.start()

# -----------------------------------------------------------------------------
# EOF
