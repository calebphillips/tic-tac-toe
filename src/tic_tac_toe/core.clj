(ns tic-tac-toe.core)

(def computer-marker :x)
(def opponent-marker :o)
(def corners #{0 2 6 8})
(def cross-points #{1 3 5 7})

(defn rows 
  "Returns a seq of 3 seqs for the rows of the board"
  [board]
  (partition 3 board))

(defn columns 
  "Returns a seq of 3 seqs for the columns of the board"
  [board]
  (for [i (range 3)] (map #(nth % i) (rows board))))

(defn diagonals 
  "Returns a seq of 2 seqs for the diagonals"
  [board]
  (let [coords [[0 4 8] [6 4 2]]]
    (map #(for [n %] (nth board n)) coords)))

(defn three-in-a-row 
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

(defn indexed-board [board]
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

(defn find-win-for [board player]
  (second 
    (first 
      (filter #(winner (first %)) (all-moves board player))))) 

(defn lonely-opponent? 
  "Returns true if coll contains only components or empty spaces"
  [coll]
  (and (some #{opponent-marker} coll) (not-any? #{computer-marker} coll)))

(defn containing-row [board position]
  (nth (rows board) (quot position 3)))

(defn containing-column [board position]
  (nth (columns board) (rem position 3)))

(defn is-trappable? 
  "Takes a board and a corner position a returns true if the only player
  in both the row and the column containing that corner is the opponent"
  [board corner]
  (every? #(lonely-opponent? (% board corner)) 
          [containing-row containing-column]))

(defn find-trappable-corners [board]
  (filter #(is-trappable? board %) 
          (filter corners (empty-cells board))))

(defn move-to-win [board]
  (find-win-for board computer-marker))

(defn move-to-block [board]
  (find-win-for board opponent-marker))

(defn move-to-center [board]
  (when-not (get-in board [4]) 4))

(defn prevent-diagonal-trap 
  "Returns a board that responds correctly to being surrounded by the
  opponent on either of the diagonals"
  [board]
  (when (= 2 (count (find-trappable-corners board)))
           (some cross-points (empty-cells board))))

(defn prevent-corner-trap 
  "Returns a move that will prevent the opponent from moving into a corner
  which would allow them to win on either the row of column containing
  that corner"
  [board]
  (first (find-trappable-corners board)))

(defn move-to-corner [board]
  (some corners (empty-cells board)))

(defn move-to-first-empty [board]
  (first (empty-cells board)))

(defn find-computer-move 
  "Returns the index of the first available move for the computer according
  to the precedence established in the playing strategy.  This is the brain."
  [board]
  (some #(% board)  [move-to-win
                    move-to-block
                    move-to-center 
                    prevent-diagonal-trap
                    prevent-corner-trap
                    move-to-corner 
                    move-to-first-empty]))

(defn move-player [board position player]
  (if (get-in board [position])
    (throw (IllegalArgumentException. "That space is already taken."))
    (assoc-in board [position] player)))

(defn move-computer 
  "Returns new board with computer's move marked"
  [board] 
  (move-player board (find-computer-move board) computer-marker))

(defn move-opponent 
  "Returns new board with the opponent's (human player) move marked"
  [board position]
  (move-player board position opponent-marker))

