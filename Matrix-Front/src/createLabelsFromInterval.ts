function createLabelsFromInterval(
  startValue: number,
  endValue: number,
  interval: number
): number[] {
  startValue = fixPrecision(startValue);
  endValue = fixPrecision(endValue);
  interval = fixPrecision(interval);

  if (endValue <= startValue) throw new Error('End value has to be greater than the start value');

  let diff = fixPrecision(endValue - startValue);
  if (!Number.isInteger(fixPrecision(diff / interval)))
    throw new Error('Interval has to divide the difference exactly');

  let labels: number[] = [];
  for (let i = startValue; i <= endValue; i = fixPrecision(i + interval)) labels.push(i);

  return labels;
}

function fixPrecision(num: number): number {
  return Number.parseFloat(num.toPrecision(3));
}

try {
  console.log(createLabelsFromInterval(1, 0.75, 0.25));
} catch (e) {
  if (e instanceof Error) console.log(e.message);
}

try {
  console.log(createLabelsFromInterval(1, 1, 0.25));
} catch (e) {
  if (e instanceof Error) console.log(e.message);
}

try {
  console.log(createLabelsFromInterval(1, 2, 0.3));
} catch (e) {
  if (e instanceof Error) console.log(e.message);
}
console.log(createLabelsFromInterval(1, 2, 0.25));
console.log(createLabelsFromInterval(0, 10, 2.5));
console.log(createLabelsFromInterval(1, 5, 0.2));
console.log(createLabelsFromInterval(1, 3, 2));
console.log(createLabelsFromInterval(1, 2.2, 0.2));
