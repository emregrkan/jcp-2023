export function normalizeScreaming(screamingCase) {
  const words = screamingCase.toLowerCase().split("_");
  for (let i = 0; i < words.length; i++) {
    words[i] = words[i].charAt(0).toUpperCase() + words[i].slice(1);
  }
  return words.join(" ");
}
