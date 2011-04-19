(ns tic-tac-toe.game-spec
    (:use [speclj.core]
          [tic-tac-toe.game]
          [clojure.string :only [join]]))

(defn to-in [& moves]
      (join "\n" moves))

(describe "Playing"
  (it "validates the input"
      (should (re-seq #"Invalid move"
                      (with-out-str
                        (with-in-str (to-in "A" 0 8 5)
                                     (game)))))  
      )

  (it "shows the winner"
      (should (re-seq #"X has won the game!" 
                       (with-out-str
                        (with-in-str (to-in 0 8 5)
                                     (game)))))  
      ) 

  (it "shows a tie message"
      (should (re-seq #"The game has ended in a tie."
                      (with-out-str
                        (with-in-str (to-in 0 8 7 2 3)
                                     (game))))))
  )
