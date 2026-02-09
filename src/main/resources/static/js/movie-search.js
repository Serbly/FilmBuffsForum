const input = document.getElementById('movieInput');
const dropdown = document.getElementById('movieDropdown');
const movieIdField = document.getElementById('movieId');

let timer;

input.addEventListener('input', () => {
    clearTimeout(timer);
    const q = input.value.trim();

    if (q.length < 2) {
        dropdown.classList.add('hidden');
        return;
    }

    timer = setTimeout(async () => {
        const res = await fetch(`/api/movies/search?q=${encodeURIComponent(q)}`);
        const data = await res.json();

        dropdown.innerHTML = data.map(m => `
            <div onclick="selectMovie('${m.id}', '${m.title}')">${m.title} (${m.releaseYear ?? ''})</div>
        `).join('');

        dropdown.classList.remove('hidden');
    }, 300);
});

function selectMovie(id, title) {
    movieIdField.value = id;
    input.value = title;
    dropdown.classList.add('hidden');
}
