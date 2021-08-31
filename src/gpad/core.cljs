(ns gpad.core
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [react-native :as rrn]
            [re-frame.core :as rf]
            ["@react-navigation/native" :refer [NavigationContainer]]
            ["@react-navigation/native-stack" :refer [createNativeStackNavigator]]

            [clojure.string :as str]
            [gpad.viewer :as viewer]
            [gpad.song-list :as song-list]))

(def nav-container (r/adapt-react-class NavigationContainer))


(defn log [d] (js/console.log d))

(def lyric
  ["[Asus4]Here's a thought for every man who tries to understand"
   "what is in his [G]hands  (what's in his hands)"
   "He [Asus4]walks along the open road of love and life"
   "surviving if he [G]can   (only if he can)"
   "-"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]Chained to all the [D]places that he [C]never wished to [G5]stay"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]As he faced the [D]sun he [C]cast no shadow"
   ""
   "[Am][C][D][E]"
   "And [D]after all,[Asus7]"])

(defn prepare-navigator
  [navigator screen]
  (fn [& params]
    (let [[props children] (if (map? (first params))
                             [(first params) (second params)]
                             [{} (first params)])]
      (into [navigator props]
            (mapv (fn [props]
                    [screen (update props :component r/reactify-component)])
                  children)))))

(defn create-stack-navigator
  []
  (let [^js stack (createNativeStackNavigator)]
    [(r/adapt-react-class (.-Navigator stack))
     (r/adapt-react-class (.-Screen stack))]))

(defn stack
  [& params]
  (let [[navigator screen] (create-stack-navigator)]
    (prepare-navigator navigator screen)))

(rf/reg-event-fx
  :navigate-to
  (fn [{:keys [db]} [_ route]]
    {:db       db
     :navigate route}))

(defonce nav-ref-state (atom nil))

(rf/reg-fx
  :navigate
  (fn [route]
    (when-let [nav-ref @nav-ref-state]
      (.navigate ^js nav-ref route))))

(def test-data
  [{:title "Masha 21" :on-click (fn []
                                  (rf/dispatch [:navigate-to :song]))}
   {:title "Dasha 11"}])

(def list-state (r/atom test-data))

(defn songs-page []
  [song-list/song-list @list-state])

(defn song-page []
  [viewer/viewer lyric])

(defn main-page []
  [rn/safe-area-view {:style {:flex 1}} #_{:style {:flex 1 :align-items "center" :justify-content "center"}}
   [nav-container {:ref #(reset! nav-ref-state %)}
    [stack
     [{:name      :main
       :component songs-page}
      {:name      :song
       :component song-page}]]]
   #_(case @page-state
     :main
     [song-list/song-list @list-state]
     :song
     [viewer/viewer lyric])

   #_[rn/flat-list {:data       [{:title "test" :id 1} {:title "test3" :id 2}]
                    :renderItem item}]
   #_[rn/text {:style {:font-size 50}} "Hello Krell!!!"]])

(defn ^:export -main [& args]
  (r/as-element [main-page]))