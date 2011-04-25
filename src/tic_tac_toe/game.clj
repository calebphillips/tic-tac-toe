(ns tic-tac-toe.game
  (:gen-class)
  (:use [tic-tac-toe.core]
        [tic-tac-toe.messages]))

(defn init-board []
  (vec (repeat 9 nil)))

; Really in need of love
(defn read-one-move [board]
  (try 
    (let [move (Integer. (read-line))] 
      (when (< -1 move 9) 
        (when (some #{move} (empty-cells board)) move)))
    (catch NumberFormatException _ nil)))

(defn read-move [board]
  (do (prompt board)
    (loop [move (read-one-move board)]
      (if move
        move
        (do
          (print (str "Invalid move, please select another move: "))
          (flush)
          (recur (read-one-move board)))))))

(defn next-move [board]
  (if (even? (count (remove nil? board)))
    (move-o board (read-move board))
    (move-x board)))

(defn print-messages [board]
  (if-let [the-winner (winner board)]
    (pr-victory-msg board the-winner)
    (when (tie? board)
      (pr-tie-msg board))))

(defn game []
  (loop [board (next-move (init-board))]
    (print-messages board)
    (when (moves-remaining? board)
      (recur (next-move board)))))

(defn -main [& args]
  (do (welcome) (game)))

