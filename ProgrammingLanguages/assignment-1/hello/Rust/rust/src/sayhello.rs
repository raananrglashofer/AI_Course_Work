use std::env;
fn sayhello() {
    let args: Vec<String> = env::args().collect();
    let user;
    if args.len() == 1 {
        user = String::from("???");
    } else{ 
        user = String::from(&args[1]);
    }
    println!("Hello {}", user);
}

fn main() {
    sayhello();
}

