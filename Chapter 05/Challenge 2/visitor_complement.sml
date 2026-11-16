(* Example: 
type expr_ops = {
    eval : unit -> int,
    pretty : unit -> string,
    optimize : unit -> expr_ops
}
*) 

fun makeNumber n = {
    eval = (fn () => n),
    pretty = (fn () => Int.toString n),
    optimize = (fn () => makeNumber n)
}

fun makeAdd (a, b) = {
    eval = (fn () => a.eval() + b.eval()),
    pretty = (fn () => "(" ^ a.pretty() ^ " + " ^ b.pretty() ^ ")"),
    optimize = (fn () =>
        let val a' = a.optimize()
            val b' = b.optimize()
        in makeAdd(a', b') end)
}
