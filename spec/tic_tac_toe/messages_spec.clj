(ns tic-tac-toe.messages-spec
  (:use [speclj.core]
        [tic-tac-toe.messages]))

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
