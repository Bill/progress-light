# progress-light

[Intervox/clj-progress](https://github.com/Intervox/clj-progress) is a fine and flexible Clojure progress bar, inspired by [node-progress](https://github.com/tj/node-progress).

The basic usage scenario is to `init` with an integer count representing the total work to be done. Then as work is done `tick` is called once for each unit of work. [Intervox/clj-progress](https://github.com/Intervox/clj-progress) will update a progress bar (defaulting to a nifty terminal-based one) with every `tick` call.

This presents a problem when the amount of work to be done at each `tick` is small relative to the cost of updating the progress bar e.g. `println` to standard out. Furthermore, it is simply wasteful to update a progress bar more often than maybe 24 times per second. For most applications, once a second often enough.

Luckily [Intervox/clj-progress](https://github.com/Intervox/clj-progress) offers a function to "zoom" progress to a position: `(tick-to x)`. If your app wants to avoid the overhead of calling `tick` for every single work item processd, it can call `tick-to` periodically, to update the progress bar in leaps.

The [Bill/progress-light](https://github.com/Bill/progress-light) library provides just this sort of optimization, but it does so behind a façade of `tick`. This means your app can continue simply calling `tick` for each work item and [Bill/progress-light](https://github.com/Bill/progress-light) will take care of remembering all the ticks and periodically calling `tick-to` on [Intervox/clj-progress](https://github.com/Intervox/clj-progress) with the ticks accumulated since the last update of the progress bar.

The end result is a much more predictable, and oftentimes more efficient use of resources.

## Installation

You can install [progress-light using clojars](https://clojars.org/progress-light) repository.

With Leiningen:

[progress-light "0.1.1"]

## Usage

Construct an new instance with `(progress-light)`. e.g.

```clojure
(def p-light (progress-light))
```

Start the progress bar by calling `(monitor-progress p-light max-ticks)` e.g.

```clojure
(monitor-progress p-light 2000)
```

Your code can call `tick` as often as it needs to, confident that the progress bar will be updated no more often than once per second:

```clojure
(tick p-light)
```

When the work is done, call `done`:

```clojure
(done p-light)
```

By default the progress bar will be updated every second. Change that default by passing a third `inform-every` (milliseconds) parameter to `monitor-progress`.

Here we start a progress bar for 2000 units of work, with progress bar updates every 5 seconds:

```clojure
(monitor-progress p-light 2000 5000)
```

## Examples

A 2-second task that does 2000 units of work:

```clojure
(require '[progress-light.core :refer :all])

(let [p-light (progress-light)]
  (monitor-progress p-light 2000)
  (dotimes [n 2000]
    (tick p-light)
    (Thread/sleep 1))
  (done p-light))
```

Since we didn't override the default `inform-every` interval, the progress bar is updated about once every second.

So instead of updating the progress bar 2000 times we only update it about two times.

## License

Copyright © 2015 Bill Burcham

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
