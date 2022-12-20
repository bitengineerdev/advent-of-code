use std::{
    collections::HashSet,
    fs::File,
    io::{self, BufRead, BufReader},
    path::Path,
};

pub fn execute() {
    let example_input = lines_from_file("./src/day_3/example_input.txt").expect("Could not load file.");
    let puzzle_input = lines_from_file("./src/day_3/puzzle_input.txt").expect("Could not load file.");
    println!("Part 1 Example: Expected: 157, Actual: {}", run_test_1(&example_input));
    println!("Part 1: {}", run_test_1(&puzzle_input));
    println!("Part 2 Example: Expected: 70, Actual: {}", run_test_2(&example_input));
    println!("Part 2: {}", run_test_2(&puzzle_input));
}

fn lines_from_file(filename: impl AsRef<Path>) -> io::Result<Vec<String>> {
    BufReader::new(File::open(filename)?).lines().collect()
}

struct Rucksack<'a> {
    compartments: [&'a str; 2],
}

fn get_points(char: char) -> u32 {
    let value = char as u32;
    if char.is_lowercase() {
        value - 96
    } else {
        value - 64 + 26
    }
}

fn build_rucksack(line: &str) -> Rucksack {
    let mid_point_idx = line.len() / 2;
    let compartment_1 = &line[..mid_point_idx];
    let compartment_2 = &line[mid_point_idx..];

    return Rucksack {
        compartments: [compartment_1, compartment_2],
    };
}

fn build_hash_set(value: &str) -> HashSet<char> {
    let mut set = HashSet::new();

    for c in value.chars() {
        set.insert(c);
    }

    return set;
}

fn find_error_in_rucksack(rucksack: &Rucksack) -> char {
    let first_compartment = build_hash_set(&rucksack.compartments[0]);

    for c in rucksack.compartments[1].chars() {
        if first_compartment.contains(&c) {
            return c;
        }
    }
    panic!("Did not find match");
}

fn run_test_1(lines: &Vec<String>) -> String {
    let total: u32 = lines
        .iter()
        .map(|line| build_rucksack(line))
        .map(|rucksack| find_error_in_rucksack(&rucksack))
        .map(|error| get_points(error))
        .sum();
    return total.to_string();
}

fn run_test_2(lines: &Vec<String>) -> String {
    let mut index: u32 = 0;
    let mut total: u32 = 0;
    let mut badges: HashSet<char> = HashSet::new();

    for line in lines {
        match index {
            0 => badges = build_hash_set(&line),
            1 | 2 => {
                let mut intersection: HashSet<char> = HashSet::new();
                for c in line.chars() {
                    if badges.contains(&c) {
                        intersection.insert(c);
                    }
                }
                badges = intersection;
            }
            _ => { panic!("We should have equal groups of 3.") }
        }

        index += 1;

        // reset if the group ends
        if index > 2 {
            for c in badges {
                total += get_points(c);
            }
            badges = HashSet::new();
            index = 0;
        }
    }

    return total.to_string();
}
