(ns tic-tac-toe.core)

(defn rows [board]
  "Returns a seq of 3 seqs for the rows of the board"
  (partition 3 board))

(defn columns [board]
  "Returns a seq of 3 seqs for the columns of the board"
  (for [i (range 3)] (map #(nth % i) (partition 3 board))))

(defn diagonals [board]
  "Returns a seq of 2 seqs for the diagonals"
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
  "Finds winning board states and returns a seq of vectors of (1) the winning state"
  "and (2) the position of the winning move.  This additional detail is"
  "using for blocking a potential win by the opponent"
  (first (filter #(winner (first %)) 
          (map #(vector (assoc-in board [%] player) %) (empty-cells board)))))

(defn move-player [board position player]
  (if (get-in board [position])
    (throw (IllegalArgumentException.))
    (assoc-in board [position] player)))

(defn find-x-win [board]
  (first (find-win-for board :x)))

(defn find-x-block [board]
  (let [win-for-o (second (find-win-for board :o))]
    (when win-for-o
      (move-player board win-for-o :x))))

(defn find-corner-move [board]
  (some #{0 2 6 8} (empty-cells board)))

(defn x-move-index [board]
  (if-not (get-in board [4])
    4
    (if-let [corner-move (find-corner-move board)]
            corner-move
            (first (empty-cells board)))))

(defn find-killer-x-move [board]
  (move-player board (x-move-index board) :x))

(defn move-x [board] 
  "Returns new board with computer's move marked"
  (if-let [winning-move (find-x-win board)]
      winning-move
      (if-let [blocking-move (find-x-block board)]
              blocking-move
              (find-killer-x-move board))))

(defn move-o [board position]
  (move-player board position :o))

(defn format-board [board]
  (doseq [r (rows board)] (println (interpose "|" (map #(if (nil? %) "  " %) r)))))

(defn- init-board []
  (vec (repeat 9 nil)))

(defn game []
  (loop [board (init-board) the-winner nil]
    (cond the-winner 
          (do 
            (println (format-board board)) 
            (println (str the-winner " won")))
          (empty? (empty-cells board)) (println "TIE!")
          :else
          (do
            (println (format-board board))
            (let [board (move-o board (Integer. (read-line)))]
              (if (winner board)
                (recur board (winner board))
                (let [board (move-x board)]
                  (recur board (winner board))))))
          )
    ))
