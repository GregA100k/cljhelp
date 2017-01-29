(ns cljhelp.core
  (:require [clojure.browser.dom :as dom]
 ))

(defn paste-value
 " help from https://gist.github.com/kohyama/6192253"
  [t]
  (let [spot (dom/get-element "spot")
        ;el (dom/get-element "text")
        el (dom/element "textarea" {:cols 50 :rows 10})
        highlighted-value t
        set-the-valude (dom/set-text el highlighted-value)
       ]
        (dom/append spot el)
    ))

(defn redirect-to-cheatsheet [url] 
  (let [spot (dom/get-element "spot")
        ifr (dom/element "iframe" {:width "100%" :height "100%" :frameborder "0"})
        set-the-url (set! (.-src ifr) url)
       ]
    (dom/append spot ifr)
  ))

(defn handle-highlight [t]
  (let [txt (aget t "data")]
  (if (= txt "page") (redirect-to-cheatsheet "https://clojure.org/api/cheatsheet")
    (paste-value txt))))
  
(defn current-tab
  "taken from https://github.com/marcelocra/chrome-extension-cljs-example/blob/master/src/chrome_extensions/background/events.cljs"
  [cb]
  (.query js/chrome.tabs
          #js {:active true :currentWindow true}
          (fn [tabs]
            (cb (first tabs)))))

(defn pasteSelection []
  (let [f1 (fn [tab] (.sendMessage js/chrome.tabs (aget tab "id") #js {:method "getSelection"} handle-highlight))
       ]
  (current-tab f1)))

(defn init []
  (pasteSelection)
)

(set! (.-onload js/window) init)
