function say_hello(name::String)
    println("Hello, $name!")
end

if length(ARGS) > 0
    say_hello(ARGS[1])
else
    say_hello("???")
end