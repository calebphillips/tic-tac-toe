(ns tic-tac-toe.board)

(defn rows 
  "Returns a seq of 3 seqs for the rows of the board"
  [board]
  (partition 3 board))

(defn columns 
  "Returns a seq of 3 seqs for the columns of the board"
  [board]
  (for [i (range 3)] (map #(nth % i) (rows board))))

(defn- diagonals 
  "Returns a seq of 2 seqs for the diagonals"
  [board]
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(defn- three-in-a-row 
  "Takes a collection of collections and returns the value of the first
  collection, if any that contains three of the same value"
  [colls]
  (->>  (map #(partition-by identity %) colls)
        (apply concat) 
        (remove #(some nil? %))
        (filter #(= 3 (count %)))
        ffirst))

(defn winner 
  "Returns the keyword for the winner, if any, otherwise returns nil"
  [board]
  (three-in-a-row (concat (rows board) (columns board) (diagonals board))))

(defn- indexed-board [board]
  (map vector (iterate inc 0) board))

(defn empty-cells [board] 
  (map first (filter #(nil? (second %)) (indexed-board board))))

(defn tie? [board]
  (and (not (winner board)) 
       (empty? (empty-cells board))))

(defn moves-remaining? [board]
  (not (or (winner board)
           (tie? board))))

(defn space-open? [board move]
  (some #{move} (empty-cells board)))

(defn all-moves 
  "Returns lazy seq of couplets representing all possibles moves and 
  resulting boards: [[board1 move1] [board2 move2]]"
  [board player]
  (map #(vector (assoc-in board [%] player) %) (empty-cells board)))

(defn move-player [board position player]
  (if (get-in board [position])
    (throw (IllegalArgumentException. "That space is already taken."))
    (assoc-in board [position] player)))
