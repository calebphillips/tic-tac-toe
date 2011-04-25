(ns tic-tac-toe.game-spec
  (:use [speclj.core]
        [tic-tac-toe.game]
        [clojure.string :only [join]]))

(defn to-input [& moves]
  (join "\n" moves))

(defn game-with-input-should [input expected-out]
  (should (re-seq expected-out
                  (with-out-str
                    (with-in-str input
                      (game))))))
(describe "Playing"
  (it "validates the input"
    (game-with-input-should (to-input "A" 1 9 6) #"Invalid move")
    (game-with-input-should (to-input 14 1 9 6) #"Invalid move")
    (game-with-input-should (to-input 1 1 9 6) #"Invalid move"))

  (it "shows the winner"
    (game-with-input-should (to-input 1 9 6) #"X has won the game!")) 

  (it "shows a tie message"
    (game-with-input-should (to-input 1 9 8 3 4) #"The game has ended in a tie.")))

(describe "A single iteration of the read loop"
  (it "reads a number in the valid range"
    (should= 4 (with-in-str "5"
                 (read-one-move (init-board)))))

  (it "returns nil if the input is not a number"
    (should= nil (with-in-str "ABC"
                   (read-one-move (init-board)))))

  (it "returns nil if the input out of range"
    (should= nil (with-in-str "0"
                   (read-one-move (init-board))))
    (should= nil (with-in-str "10"
                   (read-one-move (init-board))))))

(describe "printing messages"
  (it "displays the winning message"
    (should (re-seq #"X has won the game"
                    (with-out-str
                      (print-status-messages [:x :x :x nil nil nil nil nil nil])))))

  (it "displays the tie message"
    (should (re-seq #"The game has ended in a tie"
                    (with-out-str
                      (print-status-messages [:x :o :x
                                       :o :x :o
                                       :o :x :o]))))))
                                  

      
