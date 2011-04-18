(ns tic-tac-toe.core)

(defn rows [board]
  (partition 3 board))

(defn columns [board]
  (for [i (range 3)] (map #(nth % i) (partition 3 board))))

(defn diagonals [board]
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(defn three-in-a-row [colls]
  "Takes a collection of collections and returns the value of the first"
  "collection, if any that contains three of the same value"
  (->>  (map #(partition-by identity %) colls)
        (apply concat) 
        (remove #(some nil? %))
        (filter #(= 3 (count %)))
        ffirst))

(defn winner [board]
  "Returns the keyword for the winner, if any, otherwise returns nil"
  (three-in-a-row (concat (rows board) (columns board) (diagonals board))))

(defn- indexed-board [board]
  (map vector (iterate inc 0) board))

(defn empty-cells [board] 
  (map first (filter #(nil? (second %)) (indexed-board board))))

(defn find-win-for [board player]
  "Finds winning board states and returns a seq of vectors of the winning state"
  "and the move that was made to get to that state.  This additional detail is"
  "using for blocking a potential win by the opponent"
  (first (filter #(winner (first %)) 
          (map #(vector (assoc-in board [%] player) %) (empty-cells board)))))

(defn find-x-win [board]
  (first (find-win-for board :x)))

(defn find-x-block [board]
  (let [win-for-o (second (find-win-for board :o))]
    (when win-for-o
      (assoc-in board [win-for-o] :x))))

(defn find-killer-x-move [board]
  (assoc-in board [(first (empty-cells board))] :x))

(defn move-x [board] 
  "Returns new board with computer's move marked"
  (let [winning-move (find-x-win board)]
    (if winning-move
      winning-move
      (let [blocking-move (find-x-block board)]
        (if blocking-move
          blocking-move
          (find-killer-x-move board))))))

(defn move-o [board position]
  (assoc-in board [position] :o))

(defn- format-board [board]
  board)

(defn- init-board []
  (vec (repeat 9 nil)))

(defn game []
  (loop [board (init-board) the-winner nil]
    (if the-winner
      (do 
        (println (str the-winner " won"))
        (println (format-board board)))
      (do
        (println (format-board board))
        (let [board (move-o board (Integer. (read-line)))]
          (println (format-board board))
          (if (winner board)
            (recur board (winner board))
            (let [board (move-x board)]
              (recur board (winner board)))))))))
