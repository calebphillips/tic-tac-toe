(ns tic-tac-toe.game
  (:gen-class)
  (:use [tic-tac-toe.core :only [space-open? move-player 
                                 winner tie? moves-remaining?]]
        [tic-tac-toe.defensive-strategy :only [find-computer-move]]
        [tic-tac-toe.messages :only [error-prompt prompt announce-victory
                                     announce-tie display-welcome]]))

; I think messages will become text-ui and become the main driver
; while this namespace will keep ui independent game functionality
(def computer-marker :x)
(def opponent-marker :o)

(defn init-board []
  (vec (repeat 9 nil)))

(defn within-range? [move]
  (< -1 move 9))

(defn is-valid? 
  "Returns whether or not this move is valid given the current state
  of the board"
  [board move]
  (and (within-range? move)
       (space-open? board move)))

(defn read-number 
  "Read one number from the player. Return the number if it is a valid move for
  the current state of the board (the space isn't already taken), otherwise nil"
  [board]
  (let [move (dec (Integer. (read-line)))] 
    (when (is-valid? board move)
      move)))

(defn read-one-move 
  "Reads one move from the human player, returning either the index of the
  move or nil if the player did not enter valid input"
  [board]
  (try (read-number board)
    (catch NumberFormatException _ nil)))

(defn print-loop-prompt 
  "Prints any additional prompts that *may* be need during
  iterations of the input loop"
  [move]
  (when-not move
    (error-prompt)))

(defn read-until-valid 
  "Reads input from the player until a valid move is entered"
  [board]
  (loop [m (read-one-move board)]
    (print-loop-prompt m)
    (if m m
      (recur (read-one-move board)))))

(defn get-opponent-move [board]
  (do 
    (prompt board)
    (read-until-valid board)))

(defn move-computer 
  "Returns new board with computer's move marked"
  [board] 
  (move-player board (find-computer-move board) computer-marker))

(defn move-opponent 
  "Returns new board with the opponent's (human player) move marked"
  [board position]
  (move-player board position opponent-marker))

(defn next-move 
  "Returns a new board containing the next move, either from the human player
  or the computer"
  [board]
  (if (even? (count (remove nil? board)))
    (move-opponent board (get-opponent-move board))
    (move-computer board)))

(defn print-status-messages 
  "Prints messages about the status of the game: ended in tie, computer won, etc."
  [board]
  (if-let [w (winner board)]
    (announce-victory board w)
    (when (tie? board)
      (announce-tie board))))

(defn game 
  "The main loop for the game, including taking user input and determining
  the outcome."
  []
  (loop [board (next-move (init-board))]
    (print-status-messages board)
    (when (moves-remaining? board)
      (recur (next-move board)))))

(defn -main [& args]
  (do (display-welcome) (game)))

