(ns tic-tac-toe.messages
  (:use [tic-tac-toe.core]
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

