(ns tic-tac-toe.messages
  (:use [tic-tac-toe.core]
        [clojure.string :only [join]]))

(defn welcome []
  (let [banner (apply str (repeat 50 "="))]
    (println (str banner 
                  "\n\n        Tic Tac Toe\n\n" 
                  banner))))

(defn format-player [p]
  (.toUpperCase (name p)))

(defn format-space [value]
  (if value
    (str " " (format-player value) " ")
    "   ")) 

(defn format-row-values [board]
  (rows (map format-space board)))

(defn format-rows [board]
  (map #(join "|" %) (format-row-values board)))

(defn format-board [board]
  (str
    "\n"
    (join "\n-----------\n" (format-rows board))
    "\n"))

(defn display-board [board]
  (println (format-board board)))

(defn pr-msg [board msg]
  (let [banner (apply str (repeat 40 "*"))] 
    (display-board board)
    (println)
    (println banner)
    (println (str "    " msg))
    (println banner)
    (println)))

(defn announce-victory [board the-winner]
  (pr-msg board (str (.toUpperCase (name the-winner)) " has won the game!")))

(defn announce-tie [board]
  (pr-msg board "The game has ended in a tie."))

(defn prompt [board]
  (do
    (display-board board)
    (print (str "Please select a move [0-8]: "))
    (flush))) 

(defn error-prompt []
  (do 
    (print (str "Invalid move, please select another move: "))
    (flush)))
