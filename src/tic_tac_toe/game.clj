(ns tic-tac-toe.game
    (:gen-class)
    (:use [tic-tac-toe.core]
          [clojure.string :only [join]]))

(defn format-board [board]
  (do 
    (println)
    (doseq [r (rows (indexed-board board))] 
           (println (interpose "|" 
                               (map #(if (nil? (second %)) 
                                       (str "   ")  
                                       (str " " (.toUpperCase (name (second %))) " ")) r))))
    (println)))

(defn- init-board []
  (vec (repeat 9 nil)))


(defn pr-msg [board msg]
      (let [banner (apply str (repeat 40 "*"))] 
        (format-board board)
        (println)
        (println banner)
        (println (str "    " msg))
        (println banner)
        (println)))

(defn pr-victory-msg [board the-winner]
      (pr-msg board (str (.toUpperCase (name the-winner)) " has won the game!")))

(defn pr-tie-msg [board]
      (pr-msg board "The game has ended in a tie."))

(defn prompt [board]
      (do
        (format-board board)
        (print (str "Please select a move (" (join "," (empty-cells board)) "): "))
        (flush))) 

; Really in need of love
(defn read-one-move [board]
      (try 
        (let [move (Integer. (read-line))] 
          (when (< -1 move 9) 
            (when (some #{move} (empty-cells board)) move)))
        (catch NumberFormatException _ nil)))

(defn read-move [board]
      (loop [move (read-one-move board)]
            (if move
              move
              (do
                (print (str "Invalid move, please select another move: "))
                (flush)
                (recur (read-one-move board))))))

(defn game []
  (loop [board (init-board) the-winner nil]
    (if the-winner 
      (pr-victory-msg board the-winner)
      (do
        (prompt board)
        (let [board (move-o board (read-move board))]
          (if (winner board)
            (recur board (winner board))
            (if (empty? (empty-cells board))
              (pr-tie-msg board)
              (let [board (move-x board)]
                (recur board (winner board))))))))))

(defn welcome []
     (let [banner (apply str (repeat 50 "="))]
       (println banner)
       (println)
       (println "        Tic Tac Toe")
       (println)
       (println banner)))

(defn -main [& args]
      (do (welcome) (game)))

