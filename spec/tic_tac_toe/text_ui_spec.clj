(ns tic-tac-toe.text-ui-spec
  (:use [speclj.core]
        [tic-tac-toe.text-ui]
        [tic-tac-toe.board :only [init-board]]
        [clojure.string :only [join]]))

(defn to-input [& moves]
  (join "\n" moves))

(defn game-with-input-should [input expected-out]
  (should (re-seq expected-out
                  (with-out-str
                    (with-in-str input
                      (game))))))

(describe 
  "Selecting whether to play as X or O"
  (it "prompts the player to choose X or O"
      (let [output (with-out-str
                     (with-in-str (to-input 1 9 6) (game)))]
        (should (re-seq #"Would you like to play as:" output))
        (should (re-seq #"1\) X" output))
        (should (re-seq #"2\) O" output))))

  ; (it "displays the correct move prompt when playing as X"
  ; (let [output (with-out-str
  ; (with-in-str (to-input 1 1 9 6) (game)))]
  ; (should (re-seq #"Please select a move for 'X'" output))))

  (it "displays the correct move prompt when playing as O"
      (let [output (with-out-str
                     (with-in-str (to-input 2 1 9 6) (game)))]
        (should (re-seq #"Please select a move for 'O'" output)))))



(describe 
  "Playing"
  (it "validates the input"
      (game-with-input-should (to-input "A" 1 9 6) #"Invalid move")
      (game-with-input-should (to-input 14 1 9 6) #"Invalid move")
      (game-with-input-should (to-input 1 1 9 6) #"Invalid move"))

  (it "shows the winner"
      (game-with-input-should (to-input 1 9 6) #"X has won the game!")) 

  (it "shows a tie message"
      (game-with-input-should (to-input 1 9 8 3 4) #"The game has ended in a tie.")))

; TODO The STDOUT prompts are muddling up the test output.  This function has
; side effects (the prompts) and a return value (the move), so I don't want to wrap
; the call in with-out-str, because I won't be able to get to my return value.
; Maybe I need to do a binding to stub out the prompt?
(describe 
  "Reading input"
  (it "reads until it gets a number" 
      (should= 8 (with-in-str (to-input "A" "B" "C" "D" 9) (read-until-valid (init-board)))))

  (it "reads until the number is in range" 
      (should= 4 (with-in-str (to-input 11 16 17 23 12312 5) (read-until-valid (init-board)))))

  (it "reads until the number is a valid move" 
      (should= 3 (with-in-str (to-input 1 2 3 4) (read-until-valid [:x :x :x nil nil nil nil nil nil]))))

  (it "decrements the number" 
      (should= 0 (with-in-str (to-input 1) (read-until-valid (init-board)))))
  )

(describe 
  "Printing messages"
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
(describe
  "Formatting a cell on the board for display"
  (it "returns spaces for a nil spot on the board"
      (should= "   " (format-cell nil)))

  (it "returns an uppercase character for a keyword"
      (should= " X " (format-cell :x))
      (should= " O " (format-cell :o))))

(describe
  "Formatting the board"
  (it "represents the current state"
      (should= "XXXOXOOOO"
               (apply str 
                      (filter #{\X \O} 
                              (format-board [:x :x :x :o :x :o :o :o :o]))))))
