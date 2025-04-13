const ctx = document.getElementById('statsChart').getContext('2d');
const audienceChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Dec 21', 'Dec 22', 'Dec 23', 'Dec 24', 'Dec 25', 'Dec 26', 'Dec 27'],
        datasets: [{
                label: 'Audience',
                data: [270000, 275000, 278000, 283000, 290000, 295000, 301000],
                backgroundColor: '#00ff99',
                borderRadius: 6,
            }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {display: false}
        },
        scales: {
            y: {
                ticks: {
                    color: '#ffffff',
                    beginAtZero: true
                }
            },
            x: {
                ticks: {
                    color: '#ffffff'
                }
            }
        }
    }
});