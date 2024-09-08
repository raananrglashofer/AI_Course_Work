use std::env;
fn sayhello() {
    let args: Vec<String> = env::args().collect();
    let mut user= String::new();
    if args.len() == 1 {
        user = String::from("???");
    } else{ 
        user = String::from(&args[1]);
    }
    println!("Hello {}", user);
}

