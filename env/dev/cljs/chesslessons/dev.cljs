(ns ^:figwheel-no-load chesslessons.dev
  (:require
    [chesslessons.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
