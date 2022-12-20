use std::{
    collections::HashMap,
    fs::File,
    io::{self, BufRead, BufReader},
    path::Path,
};

pub fn execute() {
    let example_input = lines_from_file("./src/day_2/example_input.txt").expect("Could not load file.");
    let puzzle_input = lines_from_file("./src/day_2/puzzle_input.txt").expect("Could not load file.");
    println!("Part 1 Example: Expected: 15, Actual: {}", run_test_1(&example_input));
    println!("Part 1: {}", run_test_1(&puzzle_input));
    println!("Part 2 Example: Expected: 12, Actual: {}", run_test_2(&example_input));
    println!("Part 2: {}", run_test_2(&puzzle_input));
}

fn lines_from_file(filename: impl AsRef<Path>) -> io::Result<Vec<String>> {
    BufReader::new(File::open(filename)?).lines().collect()
}

struct HandSignAttributes {
    points: i32,
    wins_against: String,
}

fn calculate_unknown_round_points(player_1: String, player_2: String) -> i32 {
    let similar_hand_signs = HashMap::from([
        (String::from('X'), String::from('A')),
        (String::from('Y'), String::from('B')),
        (String::from('Z'), String::from('C')),
    ]);

    return calculate_round_points(player_1, similar_hand_signs[&player_2].clone())
}

fn calculate_known_round_points(player_1: String, player_2: String) -> i32 {
    let similar_hand_signs = HashMap::from([
        (String::from("AX"), String::from('C')),
        (String::from("AY"), String::from('A')),
        (String::from("AZ"), String::from('B')),
        (String::from("BX"), String::from('A')),
        (String::from("BY"), String::from('B')),
        (String::from("BZ"), String::from('C')),
        (String::from("CX"), String::from('B')),
        (String::from("CY"), String::from('C')),
        (String::from("CZ"), String::from('A')),
    ]);

    let mut combined = player_1.clone();
    combined.push_str(&player_2);

    return calculate_round_points(player_1, similar_hand_signs[&combined].clone())
}

fn calculate_round_points(player_1: String, player_2: String) -> i32 {
    let hand_signs = HashMap::from([
        (
            "A".to_string(),
            HandSignAttributes {
                points: 1,
                wins_against: "C".to_string(),
            },
        ),
        (
            "B".to_string(),
            HandSignAttributes {
                points: 2,
                wins_against: "A".to_string(),
            },
        ),
        (
            "C".to_string(),
            HandSignAttributes {
                points: 3,
                wins_against: "B".to_string(),
            },
        ),
    ]);

    let player_2_hand_sign = &hand_signs[&player_2];

    let outcome_points = if player_1 == player_2_hand_sign.wins_against {
        6
    } else if player_1 == player_2 {
        3
    } else {
        0
    };

    return outcome_points + player_2_hand_sign.points;
}

fn run_test_1(lines: &Vec<String>) -> String {
    let total: i32 = lines
        .iter()
        .map(|line| line.split(' ').collect())
        .map(|players: Vec<&str>| {
            calculate_unknown_round_points(players[0].to_string(), players[1].to_string())
        })
        .sum();

    return total.to_string();
}

fn run_test_2(lines: &Vec<String>) -> String {
    let total: i32 = lines
        .iter()
        .map(|line| line.split(' ').collect())
        .map(|players: Vec<&str>| {
            calculate_known_round_points(players[0].to_string(), players[1].to_string())
        })
        .sum();

    return total.to_string();
}
