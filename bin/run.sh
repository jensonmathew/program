#!/bin/bash
script_path=$(dirname "$(realpath "$0")")
clear
echo ===============================================
echo Executing suite "$3"
echo ===============================================
lib_path=$(realpath "$script_path"/../lib)
cd "${lib_path}" || exit

java -DbuildMode="prod" -DexecutionId="$1" -DgroupExecutionID="$2" -jar tep_codeless_plugins.jar -suite="$3" -datasheet="$4"

echo ===============================================
echo Collecting log files
echo ===============================================

parent_dir=$(realpath "$script_path"/../)
cd "$parent_dir" || exit

rm -rf logs
mkdir -p logs
mv -f "$parent_dir/lib/etrlink" "$parent_dir/logs"
mv -f "$parent_dir/lib/console.log" "$parent_dir/logs"
mv -f "$parent_dir/lib/suites" "$parent_dir/logs/debug"
mv -f "$parent_dir/lib/target/debug.log" "$parent_dir/logs/debug"
mv -f "$parent_dir/lib/target/screenshots" "$parent_dir/logs"
rm -rf "$parent_dir/lib/target"
rm -rf "$parent_dir/lib/test-output"
