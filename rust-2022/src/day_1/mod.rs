use std::{
    fs::File,
    io::{self, BufRead, BufReader},
    path::Path,
};

pub fn execute() {
    let example_input = lines_from_file("./src/day_1/example_input.txt").expect("Could not load file.");
    let puzzle_input = lines_from_file("./src/day_1/puzzle_input.txt").expect("Could not load file.");
    println!("Part 1 Example: Expected: 24000, Actual: {}", run_test_1(&example_input));
    println!("Part 1: {}", run_test_1(&puzzle_input));
    println!("Part 2 Example: Expected: 41000, Actual: {}", run_test_2(&example_input));
    println!("Part 2: {}", run_test_2(&puzzle_input));
}

fn lines_from_file(filename: impl AsRef<Path>) -> io::Result<Vec<String>> {
    BufReader::new(File::open(filename)?).lines().collect()
}

fn run_test_1(lines: &Vec<String>) -> String {
    let mut previously_on_elf = false;
    let mut current_calorie = 0;
    let mut most_calories = 0;

    for line in lines {
        if line == "" {
            if previously_on_elf {
                if most_calories < current_calorie {
                    most_calories = current_calorie;
                }
                current_calorie = 0;
                previously_on_elf = false;
            }
        } else {
            let calorie = line.parse::<i32>().unwrap();
            current_calorie = current_calorie + calorie;
            previously_on_elf = true
        }
    }

    return most_calories.to_string();
}

fn run_test_2(lines: &Vec<String>) -> String {
    let mut calories: Vec<i32> = vec![];
    let mut current_calorie = 0;

    for line in lines {
        if line == "" {
            calories.push(current_calorie);
            current_calorie = 0;
        } else {
            current_calorie = current_calorie + line.parse::<i32>().unwrap()
        }
    }

    calories.sort();

    let mut total_calories = 0;

    for n in 1..4 {
        total_calories = total_calories + calories[calories.len() - n]
    }

    return total_calories.to_string();
}
