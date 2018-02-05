(ns chesslessons.prod
  (:require
    [chesslessons.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
