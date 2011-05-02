(ns tic-tac-toe.defensive-strategy
  (:use [tic-tac-toe.board :only [winner all-moves rows columns empty-cells]]))

(def corners #{0 2 6 8})
(def cross-points #{1 3 5 7})

(defn- find-win-for [board player]
  (second 
    (first 
      (filter #(winner (first %)) (all-moves board player))))) 

(defn lonely-opponent? 
  "Returns true if coll contains only components or empty spaces"
  [coll computer-marker opponent-marker]
  (and (some #{opponent-marker} coll) (not-any? #{computer-marker} coll)))

(defn containing-row [board position]
  (nth (rows board) (quot position 3)))

(defn containing-column [board position]
  (nth (columns board) (rem position 3)))

(defn is-trappable? 
  "Takes a board and a corner position a returns true if the only player
  in both the row and the column containing that corner is the opponent"
  [board corner computer-marker opponent-marker]
  (every? #(lonely-opponent? (% board corner) computer-marker opponent-marker) 
          [containing-row containing-column]))

(defn- find-trappable-corners [board computer-marker opponent-marker]
  (filter #(is-trappable? board % computer-marker opponent-marker) 
          (filter corners (empty-cells board))))

(defn- move-to-win [computer-marker board]
  (find-win-for board computer-marker))

(defn- move-to-block [opponent-marker board]
  (find-win-for board opponent-marker))

(defn- move-to-center [board]
  (when-not (get-in board [4]) 4))

(defn prevent-diagonal-trap 
  "Returns a board that responds correctly to being surrounded by the
  opponent on either of the diagonals"
  [computer-marker opponent-marker board]
  (when (= 2 (count (find-trappable-corners board computer-marker opponent-marker)))
           (some cross-points (empty-cells board))))

(defn- prevent-corner-trap 
  "Returns a move that will prevent the opponent from moving into a corner
  which would allow them to win on either the row of column containing
  that corner"
  [computer-marker opponent-marker board]
  (first (find-trappable-corners board computer-marker opponent-marker)))

(defn- move-to-corner [board]
  (some corners (empty-cells board)))

(defn- move-to-first-empty [board]
  (first (empty-cells board)))

(defn find-computer-move 
  "Returns the index of the first available move for the computer according
  to the precedence established in the playing strategy.  This is the brain."
  [board computer-marker opponent-marker]
  (some #(% board)  [(partial move-to-win computer-marker)
                     (partial move-to-block opponent-marker)
                     move-to-center 
                     (partial prevent-diagonal-trap computer-marker opponent-marker)
                     (partial prevent-corner-trap computer-marker opponent-marker)
                     move-to-corner 
                     move-to-first-empty]))
