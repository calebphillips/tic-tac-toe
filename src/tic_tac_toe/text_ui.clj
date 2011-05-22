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
    (println format-board board)
    (println)
    (println banner)
    (println (str "    " msg))
    (println banner)
    (println)))

(defn announce-victory [board the-winner]
  (annouce board (str (format-player the-winner) " has won the game!")))

(defn announce-tie [board]
  (annouce board "The game has ended in a tie."))

(defn- prompt [s]
  (print s)
  (flush))

(defn display-welcome []
  (let [banner (apply str (repeat 50 "="))]
    (println (str banner 
                  "\n\n        Tic Tac Toe\n\n" 
                  banner))))

(defn print-status-messages 
  "Prints messages about the status of the game: ended in tie, computer won, etc."
  [board]
  (if-let [w (winner board)]
    (announce-victory board w)
    (when (tie? board)
      (announce-tie board))))

(defn read-number []
  (try (Integer. (read-line))
    (catch NumberFormatException _ nil)))

(defn read-until-valid 
  "Reads input from the player until a valid move is entered"
  [board mark]
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

(def game-types 
  { 1 [{:mark :x :mover move-human} {:mark :o :mover move-computer}] 
    2 [{:mark :x :mover move-computer} {:mark :o :mover move-human}]
    3 [{:mark :x :mover move-computer} {:mark :o :mover move-computer}] })

(defn get-players []
  (println)
  (println "Would you like to play as:")
  (println "1) X")
  (println "2) O")
  (println "3) Neither - let the computer duke it out.")
  (println)
  (prompt "Choice: ")
  (game-types (read-number)))


(defn move [board current next]
  ((:mover current) board (:mark current)))

(defn game 
  "The main loop for the game, including taking user input and determining
  the outcome."
  ([]
   (let [board (init-board) [player1 player2] (get-players)]
     (println format-board board)
     (game (move board player1 player2) player2 player1)))
  ([board this-player next-player]
   (println format-board board)
   (print-status-messages board)
   (when (moves-remaining? board)
     (recur (move board this-player next-player) next-player this-player))))

(defn -main [& args]
  (do (display-welcome) (game)))

