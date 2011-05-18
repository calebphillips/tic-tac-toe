(ns tic-tac-toe.text-ui
  (:gen-class)
  (:use [tic-tac-toe.board :only [rows move-player is-valid? init-board winner tie? moves-remaining?]]
        [tic-tac-toe.defensive-strategy :only [find-computer-move]]
        [clojure.string :only [join]]))

(def computer-marker :x)
(def opponent-marker :o)

(defn format-player [p]
  (.toUpperCase (name p)))

(defn format-cell [value]
  (if value
    (str " " (format-player value) " ")
    "   ")) 

(defn format-row-values [board]
  "Returns the board divided into 3 rows where each row contains"
  "the 3 cells formatted for output"
  (rows (map format-cell board)))

(defn format-rows [board]
  (map #(join "|" %) (format-row-values board)))

(defn format-board [board]
  (str
    "\n"
    (join "\n-----------\n" (format-rows board))
    "\n"))

(defn display-board [board]
  (println (format-board board)))

(defn annouce [board msg]
  (let [banner (apply str (repeat 40 "*"))] 
    (display-board board)
    (println)
    (println banner)
    (println (str "    " msg))
    (println banner)
    (println)))

(defn announce-victory [board the-winner]
  (annouce board (str (format-player the-winner) " has won the game!")))

(defn announce-tie [board]
  (annouce board "The game has ended in a tie."))

(defn prompt [board]
  (do
    (display-board board)
    (print "Please select a move for 'O' [1-9]: ")
    (flush))) 

(defn error-prompt []
  (do 
    (print "Invalid move, please select another move: ")
    (flush)))

(defn display-welcome []
  (let [banner (apply str (repeat 50 "="))]
    (println (str banner 
                  "\n\n        Tic Tac Toe\n\n" 
                  banner))))

(defn print-loop-prompt 
  "Prints any additional prompts that *may* be need during
  iterations of the input loop"
  [move]
  (when-not move
    (error-prompt)))

(defn print-status-messages 
  "Prints messages about the status of the game: ended in tie, computer won, etc."
  [board]
  (if-let [w (winner board)]
    (announce-victory board w)
    (when (tie? board)
      (announce-tie board))))

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

(defn move-opponent 
  "Returns new board with the opponent's (human player) move marked"
  [board]
  (move-player board (get-opponent-move board) opponent-marker))

(defn move-computer 
  "Returns new board with computer's move marked"
  [board] 
  (move-player board (find-computer-move board computer-marker opponent-marker) computer-marker))

(defn game 
  "The main loop for the game, including taking user input and determining
  the outcome."
  ([]
   (game (move-opponent (init-board)) move-computer move-opponent))
  ([board this-move next-move]
   (print-status-messages board)
   (when (moves-remaining? board)
     (recur (this-move board) next-move this-move))))

(defn -main [& args]
  (do (display-welcome) (game)))

