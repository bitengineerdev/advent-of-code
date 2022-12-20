use std::{
    fs::File,
    io::{self, BufRead, BufReader},
    ops::RangeInclusive,
    path::Path,
};

pub fn execute() {
    let example_input = lines_from_file("./src/day_4/example_input.txt").expect("Could not load file.");
    let puzzle_input = lines_from_file("./src/day_4/puzzle_input.txt").expect("Could not load file.");
    println!("Part 1 Example: Expected: 2, Actual: {}", run_test_1(&example_input));
    println!("Part 1: {}", run_test_1(&puzzle_input));
    println!("Part 2 Example: Expected: 4, Actual: {}", run_test_2(&example_input));
    println!("Part 2: {}", run_test_2(&puzzle_input));
}

fn lines_from_file(filename: impl AsRef<Path>) -> io::Result<Vec<String>> {
    BufReader::new(File::open(filename)?).lines().collect()
}

fn build_ranges(line: &str) -> (RangeInclusive<u64>, RangeInclusive<u64>) {
    let ranges: Vec<u64> = line.split(',')
        .flat_map(|range| range.split('-'))
        .map(|value: &str| value.parse::<u64>().unwrap())
        .collect();
    return (ranges[0]..=ranges[1], ranges[2]..=ranges[3]);
}

fn range_fully_consumes_range(ranges: &(RangeInclusive<u64>, RangeInclusive<u64>)) -> bool {
    if ranges.0.start() > ranges.1.start() {
        ranges.1.end() >= ranges.0.end()
    } else if ranges.0.start() < ranges.1.start() {
        ranges.0.end() >= ranges.1.end()
    } else {
        true
    }
}

fn range_overlaps_with_range(ranges: &(RangeInclusive<u64>, RangeInclusive<u64>)) -> bool {
    if ranges.0.start() < ranges.1.start() {
        ranges.0.end() >= ranges.1.start()
    } else if ranges.0.start() > ranges.1.start() {
        ranges.1.end() >= ranges.0.start()
    } else {
        true
    }
}

fn run_test_1(lines: &Vec<String>) -> String {
    let total = lines.iter()
        .map(|line| build_ranges(line))
        .filter(|ranges| range_fully_consumes_range(ranges))
        .count();
    return total.to_string()
}

fn run_test_2(lines: &Vec<String>) -> String {
    let total = lines.iter()
        .map(|line| build_ranges(line))
        .filter(|ranges| range_overlaps_with_range(ranges))
        .count();
    return total.to_string()
}
