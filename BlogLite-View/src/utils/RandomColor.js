const colors = ["LightSkyBlue", "LightGreen", "LightSteelBlue"];

export function getRandomColor() {
    const index = Math.floor(Math.random() * colors.length);
    return colors[index];
}