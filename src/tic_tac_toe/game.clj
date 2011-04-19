(ns tic-tac-toe.game
    (:use tic-tac-toe.core))

(defn format-board [board]
  (doseq [r (rows board)] (println (interpose "|" (map #(if (nil? %) "  " %) r)))))

(defn- init-board []
  (vec (repeat 9 nil)))

(defn pr-victory-msg [board the-winner]
      (do 
        (println (format-board board))
        (println (str (.toUpperCase (name the-winner)) " has won the game!"))))

(defn prompt [board]
      (do
        (println (format-board board))
        (print "Please select a move: ")
        (flush))) 

(defn read-one-move []
      (try 
        (Integer. (read-line))
        (catch NumberFormatException _ nil)))

(defn read-move []
      (loop [move (read-one-move)]
            (if move
              move
              (do
                (print (str "Invalid move, please select another move: "))
                (flush)
                (recur (read-one-move))))))

(defn game []
  (loop [board (init-board) the-winner nil]
    (if the-winner 
      (pr-victory-msg board the-winner)
      (do
        (prompt board)
        (let [board (move-o board (read-move))]
          (if (winner board)
            (recur board (winner board))
            (if (empty? (empty-cells board))
              (println "The game has ended in a tie.")
              (let [board (move-x board)]
                (recur board (winner board))))))))))
