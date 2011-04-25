(ns tic-tac-toe.game
  (:gen-class)
  (:use [tic-tac-toe.core]
        [tic-tac-toe.messages]))

(defn init-board []
  (vec (repeat 9 nil)))

(defn within-range? [move]
  (< -1 move 9))

(defn is-valid? [board move]
  "Returns whether or not this move is valid given the current state"
  "of the board"
  (and (within-range? move)
       (space-open? board move)))

(defn read-number [board]
  (let [move (dec (Integer. (read-line)))] 
    (when (is-valid? board move)
      move)))

(defn read-one-move [board]
  (try (read-number board)
    (catch NumberFormatException _ nil)))

(defn print-addtl-prompt [move]
  "Prints any additional prompts that may be need during"
  "iterations of the input loop"
  (when-not move
    (error-prompt)))

(defn read-until-valid [board]
  "Reads input from the player until a valid move is entered"
  (loop [m (read-one-move board)]
    (print-addtl-prompt m)
    (if m m
      (recur (read-one-move board)))))

(defn get-opponent-move [board]
  (do 
    (prompt board)
    (read-until-valid board)))

(defn next-move [board]
  (if (even? (count (remove nil? board)))
    (move-opponent board (get-opponent-move board))
    (move-computer board)))

(defn print-messages [board]
  (if-let [w (winner board)]
    (announce-victory board w)
    (when (tie? board)
      (announce-tie board))))

(defn game []
  (loop [board (next-move (init-board))]
    (print-messages board)
    (when (moves-remaining? board)
      (recur (next-move board)))))

(defn -main [& args]
  (do (welcome) (game)))

