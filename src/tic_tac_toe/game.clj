(ns tic-tac-toe.game
  (:gen-class)
  (:use [tic-tac-toe.core]
        [tic-tac-toe.messages]))


(defn- init-board []
  (vec (repeat 9 nil)))

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



(defn -main [& args]
  (do (welcome) (game)))

