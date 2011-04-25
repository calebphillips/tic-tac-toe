(ns tic-tac-toe.messages
  (:use [tic-tac-toe.core]
        [clojure.string :only [join]]))

(defn welcome []
  (let [banner (apply str (repeat 50 "="))]
    (println (str banner "\n\n        Tic Tac Toe\n\n" banner))))

(defn format-space [value]
  (if (nil? value)
    "   "  
    (str " " (.toUpperCase (name value)) " "))) 

(defn format-board [board]
  (do 
    (println)
    (doseq [r (rows board)] 
      (println (interpose "|" (map format-space r))))
    (println)))


(defn pr-msg [board msg]
  (let [banner (apply str (repeat 40 "*"))] 
    (format-board board)
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
    (format-board board)
    (print (str "Please select a move (" (join "," (empty-cells board)) "): "))
    (flush))) 
