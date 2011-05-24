(ns tic-tac-toe.text-ui
  (:gen-class)
  (:use [tic-tac-toe.board :only [rows move-player valid-move? init-board winner tie? moves-remaining?]]
        [tic-tac-toe.defensive-strategy :only [find-computer-move]]
        [clojure.string :only [join]]))

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
  (str "\n" (join "\n-----------\n" (format-rows board)) "\n"))

(defn annouce [board msg]
  (let [banner (apply str (repeat 40 "*"))] 
    (println (format-board board))
    (println)
    (println banner)
    (println (str "    " msg))
    (println banner)
    (println)))

(defn announce-victory [board the-winner]
  (annouce board (str (format-player the-winner) " has won the game!")))

(defn announce-tie [board]
  (annouce board "The game has ended in a tie."))

(defn print-results
  [board]
  (if-let [w (winner board)]
    (announce-victory board w)
    (announce-tie board)))

(defn read-number []
  (try (Integer. (read-line))
    (catch NumberFormatException _ nil)))

(defn- prompt [s]
  (print s)
  (flush))

(defn read-until-valid 
  "Reads input from the player until a valid move is entered"
  [board mark]
  (println (format-board board))
  (prompt (str "Please select a move for '" (format-player mark) "' [1-9]: "))
  (let [input (read-number)]
    (if (and input (valid-move? board (dec input)))
      (dec input)
      (do
        (println "Invalid move.")
        (recur board mark)))))

(defn move-human [board mark]
  (move-player board (read-until-valid board mark) mark))

(defn move-computer [board mark] 
  (move-player board (find-computer-move board mark) mark))

(defn move-computer-and-display [board mark]
  (let [board (move-computer board mark)]
    (println (format-board board))
    board))

(def game-types 
  { 1 [{:mark :x :mover move-human} {:mark :o :mover move-computer}] 
   2 [{:mark :x :mover move-computer} {:mark :o :mover move-human}]
   3 [{:mark :x :mover move-computer-and-display} {:mark :o :mover move-computer-and-display}] })

(defn get-players []
  (println)
  (println "Select game type:")
  (println "1) Human    vs. Computer")
  (println "2) Computer vs. Human")
  (println "3) Computer vs. Computer")
  (println)
  (prompt "Choice: ")
  (let [game-type (game-types (read-number))]
    (if game-type
      game-type
      (recur))))

(defn move [board current next]
  ((:mover current) board (:mark current)))

(defn game 
  "The main loop for the game, including taking user input and determining
  the outcome."
  ([]
   (let [board (init-board) [player1 player2] (get-players)]
     (game board player1 player2)))
  ([board this-player next-player]
   (let [board (move board this-player next-player)]
     (if (moves-remaining? board)
       (recur board next-player this-player)
       (print-results board)))))

(defn display-welcome []
  (let [banner (apply str (repeat 50 "="))]
    (println (str banner 
                  "\n\n        Tic Tac Toe\n\n" 
                  banner))))

(defn -main [& args]
  (do (display-welcome) (game)))
