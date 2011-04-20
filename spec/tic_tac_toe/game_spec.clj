(ns tic-tac-toe.game-spec
    (:use [speclj.core]
          [tic-tac-toe.game]
          [clojure.string :only [join]]))

(defn to-in [& moves]
      (join "\n" moves))

(defn game-with-input-should [input expected-out]
      (should (re-seq expected-out
                      (with-out-str
                        (with-in-str input
                                     (game))))))
(describe "Playing"
  (it "validates the input"
      (game-with-input-should (to-in "A" 0 8 5) #"Invalid move")
      (game-with-input-should (to-in 14 0 8 5) #"Invalid move")
      (game-with-input-should (to-in 0 0 8 5) #"Invalid move"))

  (it "shows the winner"
      (game-with-input-should (to-in 0 8 5) #"X has won the game!") ) 

  (it "shows a tie message"
      (game-with-input-should (to-in 0 8 7 2 3) #"The game has ended in a tie."))
  )
