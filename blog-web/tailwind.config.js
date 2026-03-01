/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // Warm palette - 温暖的编辑风格
        cream: {
          50: '#fefdfb',
          100: '#fdf9f3',
          200: '#f9f1e4',
          300: '#f3e5d0',
          400: '#e8d4b8',
        },
        ink: {
          50: '#f7f7f6',
          100: '#e8e6e3',
          200: '#d3cfc9',
          300: '#b8b2a9',
          400: '#9a9287',
          500: '#7d7467',
          600: '#635a4e',
          700: '#4e473e',
          800: '#3d3830',
          900: '#28241e',
          950: '#1a1714',
        },
        amber: {
          400: '#f5a623',
          500: '#e8941f',
          600: '#d4821b',
        },
        coral: {
          400: '#ff7b54',
          500: '#f06543',
          600: '#e05235',
        },
        sage: {
          400: '#8fb996',
          500: '#7ba382',
          600: '#678d6d',
        },
      },
      fontFamily: {
        // 使用更有特色的字体组合
        serif: ['Noto Serif SC', 'Source Serif Pro', 'Georgia', 'serif'],
        sans: ['Source Sans 3', 'Noto Sans SC', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'Fira Code', 'Consolas', 'monospace'],
        display: ['Playfair Display', 'Noto Serif SC', 'serif'],
      },
      fontSize: {
        '2xs': ['0.625rem', { lineHeight: '0.875rem' }],
      },
      spacing: {
        '18': '4.5rem',
        '22': '5.5rem',
        '30': '7.5rem',
      },
      borderRadius: {
        '4xl': '2rem',
        '5xl': '2.5rem',
      },
      boxShadow: {
        'soft': '0 2px 15px -3px rgba(0, 0, 0, 0.07), 0 10px 20px -2px rgba(0, 0, 0, 0.04)',
        'soft-lg': '0 10px 40px -10px rgba(0, 0, 0, 0.1), 0 2px 10px -2px rgba(0, 0, 0, 0.04)',
        'soft-xl': '0 25px 50px -12px rgba(0, 0, 0, 0.12)',
        'glow': '0 0 40px -10px rgba(245, 166, 35, 0.3)',
        'inner-soft': 'inset 0 2px 4px 0 rgba(0, 0, 0, 0.04)',
      },
      typography: {
        DEFAULT: {
          css: {
            maxWidth: 'none',
            color: '#3d3830',
            '--tw-prose-body': '#3d3830',
            '--tw-prose-headings': '#1a1714',
            '--tw-prose-links': '#d4821b',
            '--tw-prose-bold': '#28241e',
            '--tw-prose-quotes': '#4e473e',
            '--tw-prose-quote-borders': '#e8941f',
            lineHeight: '1.8',
            a: {
              color: '#d4821b',
              textDecoration: 'none',
              backgroundImage: 'linear-gradient(120deg, #f5a623 0%, #f5a623 100%)',
              backgroundRepeat: 'no-repeat',
              backgroundSize: '100% 0.1em',
              backgroundPosition: '0 88%',
              transition: 'background-size 0.25s ease-in',
              '&:hover': {
                backgroundSize: '100% 88%',
              },
            },
            h1: {
              fontFamily: 'Playfair Display, Noto Serif SC, serif',
              fontWeight: '700',
              letterSpacing: '-0.02em',
            },
            h2: {
              fontFamily: 'Playfair Display, Noto Serif SC, serif',
              fontWeight: '600',
              letterSpacing: '-0.01em',
            },
            h3: {
              fontFamily: 'Source Sans 3, Noto Sans SC, sans-serif',
              fontWeight: '600',
            },
            code: {
              color: '#e05235',
              backgroundColor: '#f9f1e4',
              padding: '0.2em 0.4em',
              borderRadius: '0.375rem',
              fontWeight: '500',
              fontSize: '0.875em',
            },
            'code::before': {
              content: '""',
            },
            'code::after': {
              content: '""',
            },
            pre: {
              backgroundColor: '#1a1714',
              borderRadius: '0.75rem',
              boxShadow: '0 10px 40px -10px rgba(0, 0, 0, 0.3)',
            },
            'pre code': {
              backgroundColor: 'transparent',
              color: '#e8e6e3',
            },
            blockquote: {
              borderLeftWidth: '4px',
              borderLeftColor: '#e8941f',
              backgroundColor: '#fdf9f3',
              borderRadius: '0 0.5rem 0.5rem 0',
              padding: '1rem 1.5rem',
              fontWeight: '500',
              fontStyle: 'normal',
            },
          },
        },
      },
      animation: {
        'fade-in': 'fadeIn 0.6s ease-out',
        'slide-up': 'slideUp 0.5s ease-out',
        'slide-in-right': 'slideInRight 0.5s ease-out',
        'scale-in': 'scaleIn 0.3s ease-out',
        'float': 'float 6s ease-in-out infinite',
        'pulse-soft': 'pulseSoft 2s ease-in-out infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { opacity: '0', transform: 'translateY(20px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        slideInRight: {
          '0%': { opacity: '0', transform: 'translateX(-20px)' },
          '100%': { opacity: '1', transform: 'translateX(0)' },
        },
        scaleIn: {
          '0%': { opacity: '0', transform: 'scale(0.95)' },
          '100%': { opacity: '1', transform: 'scale(1)' },
        },
        float: {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-10px)' },
        },
        pulseSoft: {
          '0%, 100%': { opacity: '1' },
          '50%': { opacity: '0.7' },
        },
      },
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
  ],
}