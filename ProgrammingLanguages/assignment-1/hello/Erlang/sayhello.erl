-module(sayhello).
-export([main/1]).

main([Name]) ->
    io:format("Hello ~s~n", [Name]);
main([]) ->
    io:format("Hello ???~n").