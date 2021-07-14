import logo from './logo.svg';
import './App.css';
import { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Link, useHistory, useParams } from 'react-router-dom'
import axios from 'axios'
import Quagga from 'quagga';

function App() {
  const [input, setInput] = useState("");
  const [readerId, setReaderId] = useState(localStorage.getItem('readerId') || -1);
  const [refresh, setRefresh] = useState(true);
  const [detects, setDetects] = useState([])
  const [name, setName] = useState(localStorage.getItem('name') || '')
  const [books, setBooks] = useState([])
  const [isLoading, toggleLoadingState] = useState(true)
  const [isSearching, toggleSearchingState] = useState(true)

  if (readerId > -1 && refresh) {
    setRefresh(false)
    setReaderId(localStorage.getItem('readerId'), -1)
  }

  const handleInput = event => {
    setInput(event.target.value);
  };

  const handleNameInput = event => {
    setName(event.target.value);
    localStorage.setItem('name', event.target.value);
  }

  function mode(arr) {
    return arr.sort((a, b) =>
      arr.filter(v => v === a).length
      - arr.filter(v => v === b).length
    ).pop();
  }

  const scanner = (history) => {
    Quagga.init({
      inputStream: {
        name: "Live",
        type: "LiveStream",
        target: document.querySelector('#scanner')
      },
      decoder: {
        readers: ["ean_reader"]
      },
    }, function (err) {
      if (err) {
        console.log(err);
        return
      }
      console.log("Initialization finished. Ready to start");
      Quagga.start();
    });

    Quagga.onDetected((data) => {
      detects[detects.length] = data.codeResult.code
      if (detects && detects.length > 15) {
        Quagga.stop()
        setInput(mode(detects))
        history.push("/search/" + mode(detects))
      }
    })
  }

  const handleRegister = name => {
    fetch("http://localhost:8080/readers", {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ name: name })
    })
      .then(response => response.json())
      .then(({ readerId }) => { localStorage.setItem('readerId', readerId); return readerId })
      .then(readerId => setReaderId(readerId))
  }

  const query = async () => {
    axios.get(`http://localhost:8080/books?query=${input}`)
         .then(x => x.data)
         .then(books => setBooks(books))
         .then(() => toggleLoadingState(false))
  }

  return (
    <Router>
      <div className="container">
        <Navigation isAuthenficated={readerId > -1} name={name} />

        <Route path="/" exact render={() => <Home value={input} handleInput={handleInput} readerId={readerId} query={query} isLoading={isLoading} books={books} toggleLoadingState={toggleLoadingState} />} />
        <Route path="/search" render={() => <Search value={input} handleInput={handleInput} query={query} readerId={readerId} isLoading={isLoading} books={books} toggleLoadingState={toggleLoadingState} />} />
        <Route path="/register" render={({ history }) => <Register handleInput={handleNameInput} name={name} handleRegister={handleRegister} history={history} />} />
        <Route path="/scanner" render={({ history }) => <Scanner history={history} detects={detects} setDetects={setDetects} scanner={scanner} handleInput={handleInput} />} />
        <Route path="/clubs" render={({ location }) => <JoinClub location={location} readerId={readerId} />} />

      </div>
    </Router>
  );
}

const JoinClub = ({ location, readerId }) => {
  const isbn = new URLSearchParams(location.search).get('isbn')
  const [clubs, setClubs] = useState([])

  useEffect(() => fetch(`http://localhost:8080/clubs?reader_id=1&isbn=${isbn}`)
    .then(x => x.json())
    .then(setClubs)
    , [])

  return (
    <>
      {clubs.map(club => <BookInClubs title={club.book.title}
        author={club.book.author}
        cover={club.book.cover}
        isbn={isbn}
        clubId={club.id}
        amountOfReaders={club.amountOfReaders}
        readerId={readerId} />)}
    </>
  )
}

const Scanner = ({ scanner, history }) => {
  useEffect(() => scanner(history))

  return (
    <div id="scanner"></div>
  )
}

const Register = ({ handleInput, handleRegister, name, history }) => (
  <>
    <form>
    </form>
    <form className="box has-background-white">
      <h2 className="title has-text-centered has-text-dark">
        Sign Up
      </h2>
      <div className="field">
        <p className="control">
          <input className="input" placeholder="Name" type="text" onInput={handleInput} />
        </p>
      </div>
      <div className="field">
        <p className="control">
          <input className="input" type="password" placeholder="Password" />
        </p>
      </div>
      <div className="field">
        <p className="control has-text-centered">
          <button className="button button_primary" onClick={event => { handleRegister(name); history.push("/") }}>Registrieren</button>
        </p>
      </div>
    </form>

  </>
)

const Search = ({ books, value, handleInput, query, readerId, isLoading, toggleLoadingState }) => {
  useEffect(query, [])

  return (
    <>
      <SearchBar value={value} handleInput={handleInput} queryBooks={query} toggleLoadingState={toggleLoadingState} />
      <main>
        {
          isLoading
            ? <div className="has-text-centered">
              <div className="loader"></div>
              <p>Wir schauen nach deinem Buch.</p>
            </div>
            : books.length === 0
              ? <p style={{ marginTop: "32px" }} className="has-text-centered">Wir haben dein Buch leider nicht gefunden.</p>
              : books.map(({ title, author, id, cover }, index) => <Book title={title} cover={cover} author={author} isbn={id} readerId={readerId} key={index} />)
        }
      </main>
    </>
  )
}

const BookInClubs = ({ readerId, title, author, cover, isbn, clubId, amountOfReaders }) => {

  const history = useHistory()

  return (
    <article className="media">
      <figure className="media-left image is-128x128">
        <img src={cover} />
      </figure>
      <div className="media-content">
        <div className="content">
          <p>
            <strong>{title}</strong>
            <br />
            von {author}
          </p>
          <p>{amountOfReaders} aktive Leser</p>
          {
            <p>
              {
                <button className="button" onClick={() =>
                  fetch(`http://localhost:8080/clubs/${clubId}`, {
                    method: 'POST',
                    headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ readerId, clubId, isbn })
                  })
                    .then(() => history.push("/"))
                }>Buchclub beitreten</button>
              }
            </p>
          }
        </div>
      </div>
    </article>
  )
}

const BookInShelf = ({ title, author, cover, isbn, amountOfClubs, clubId, readerId, isClubmember }) => {

  const history = useHistory()

  return (
    <article className="media">
      <figure className="media-left image is-128x128">
        <img src={cover} />
      </figure>
      <div className="media-content">
        <div className="content">
          <p>
            <strong>{title}</strong>
            <br />
            von {author}
          </p>
          {
            isClubmember
              ? <p>Du bist bereits Mitglied dieses Clubs</p>
              : <p>
                {
                  amountOfClubs === 0
                    ? <button className="button button_secondary" onClick={() =>
                      fetch(`http://localhost:8080/clubs/${clubId}`, {
                        method: 'POST',
                        headers: {
                          'Accept': 'application/json',
                          'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ readerId, clubId, isbn })
                      })
                        .then(() => history.go(0))
                    }>Buchclub gründen</button>
                    : amountOfClubs > 1
                      ? <Link to={{ pathname: "/clubs", search: `?isbn=${isbn}` }}>{amountOfClubs + " aktive Clubs"}</Link>
                      : <Link to={{ pathname: "/clubs", search: `?isbn=${isbn}` }}>{amountOfClubs + " aktiver Club"}</Link>
                }
              </p>
          }
        </div>
      </div>
    </article>
  )
}

const Book = ({ title, author, cover, isbn, readerId }) => (
  <article className="media" style={{ marginBottom: "32px" }}>
    <figure className="media-left image is-128x128">
      <img src={cover} />
    </figure>
    <div className="media-content">
      <div className="content">
        <p>
          <strong>{title}</strong>
          <br />
          von {author}
        </p>
        <button
          className="button"
          onClick={(event) => {
            fetch("http://localhost:8080/shelfs", {
              method: 'POST',
              headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({ readerId, isbn, author, cover, title })
            })

            event.target.innerHTML = "Bereits in deinem Bücherregal"
            event.target.disabled = true
          }
          } >
          zum Buchregal hinzufügen
        </button>
      </div>
    </div>
  </article>
)

const Home = ({ handleInput, value, readerId, query, isLoading, books, toggleLoadingState }) => (
  <>
    <SearchBar handleInput={handleInput} value={value} queryBooks={query} isLoading={isLoading} books={books} toggleLoadingState={toggleLoadingState} />
    <main style={{ height: "80vh" }} className="main is-flex is-flex-direction-column is-justify-content-space-around">
      <Clubs readerId={readerId} />
      <Shelf readerId={readerId} />
    </main>
  </>
)

const Navigation = ({ isAuthenficated, name }) => (
  <nav className="navbar" role="navigation" aria-label="main navigation">
    <div className="navbar-brand">
      <Link className="navbar-item logo" to="/" >
        we<b>read.</b>
      </Link>

      <a role="button" className="navbar-burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div className="navbar-menu">
      {
        !isAuthenficated ?
          <div className="navbar-end">
            <div className="navbar-item">
              <div className="buttons">
                <a className="button is-light">
                  Log in
                </a>
                <Link to="/register" className="button button_secondary">
                  <strong>Sign up</strong>
                </Link>
              </div>
            </div>
          </div>
          :
          <div className="navbar-end">
            <div className="navbar-item">
              <p>Willkommen {name}</p>
            </div>
          </div>
      }
    </div>
  </nav>
)

const SearchBar = ({ value, handleInput, queryBooks, toggleLoadingState }) => {
  const history = useHistory();
  return (
    <div className="field has-addons searchbar">
      <input className="input" type="text" placeholder="ISBN" value={value} onChange={handleInput} />
      <Link className="button button_secondary" to={"/scanner/"}>
        Scan
      </Link>
      <button className="button button_primary" onClick={() => { toggleLoadingState(true); queryBooks(); history.push("/search/" + value) }}>
        Suchen
      </button>
    </div>
  )
}

const Clubs = ({ readerId }) => {

  const [clubs, setClubs] = useState([])

  useEffect(() => {
    fetch(`http://localhost:8080/clubs/${readerId}`)
      .then(x => x.json())
      .then(setClubs)
  }, [])

  return (
    <section>
      <header>
        <h1>Buchclub</h1>
      </header>
      {
        clubs.length === 0
          ? <EmptyClub />
          : <FilledClubs clubs={clubs} />
      }
    </section>
  )
}

const EmptyClub = () => (
  <>
    <p className="dashboard_no_books">Du bist bisher in noch keinem Buchclub.</p>
    <a>Finde einen Buchclub</a>
  </>
)

const FilledClubs = ({ clubs }) => {
  console.log(clubs[0])
  return (
    <>
      {clubs.map(({ book, url }) => ({ author: book.author, cover: book.cover, title: book.title, url })).map(({ author, cover, title, url }, index) =>
        <BookInClub
          author={author}
          cover={cover}
          title={title}
          roomUrl={url}
          key={index}
        />)
      }
    </>
  )
}

const BookInClub = ({ title, author, cover, roomUrl }) => {
  return (
    <article className="media">
      <figure className="media-left image is-128x128">
        <img src={cover} />
      </figure>
      <div className="media-content">
        <div className="content">
          <p>
            <strong>{title}</strong>
            <br />
            von {author}
          </p>
          <a className="button button_secondary" href={roomUrl}>Zur Diskussion</a>
        </div>
      </div>
    </article>
  )
}

const Shelf = ({ readerId }) => {
  const [books, setBooks] = useState([])

  useEffect(() => {
    fetch(`http://localhost:8080/shelfs?reader_id=${readerId}`)
      .then(x => x.json())
      .then(setBooks)
  }, [])

  return (
    <section>
      <header>
        <h1>Bücherregal</h1>
      </header>
      {
        books.length === 0
          ? <EmptyShelf />
          : <FilledShelf books={books} readerId={readerId} />
      }
    </section>
  )
}

const EmptyShelf = () => (
  <>
    <p className="dashboard_no_books">Dein Bücherregal ist bisher noch leer.</p>
    <a>Finde interessante Bücher</a>
  </>
)

const FilledShelf = ({ books, readerId }) => (
  <>
    {books.map(({ author, cover, title, amountOfClubs, id, isClubMember, clubId }, index) =>
      <BookInShelf
        author={author}
        cover={cover}
        title={title}
        isbn={id}
        amountOfClubs={amountOfClubs}
        clubId={clubId}
        readerId={readerId}
        isClubmember={isClubMember}
        key={index}
      />)
    }
  </>
)

const NextDiskussion = () => (
  <section className="container level navbar is-fixed-bottom">
    <div className="level-left">
      <div>
        <p>24.Juli</p>
        <p>10:00 Uhr</p>
      </div>
    </div>
    <div>
      <button className="button" disabled>Diskussion beitreten</button>
    </div>
  </section>
)


export default App;
